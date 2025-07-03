package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.FotoOcorrenciaDTO;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.dto.OcorrenciaDTOs;
import br.ufsm.smpp.model.Incidente;
import br.ufsm.smpp.model.Ocorrencia;
import br.ufsm.smpp.repository.OcorrenciaRepository;
import br.ufsm.smpp.model.FotoOcorrencia;
import br.ufsm.smpp.model.TipoOcorrencia;
import br.ufsm.smpp.model.Propriedade;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final PropriedadeService propriedadeService;
    private final IncidenteService incidenteService;
    private final TipoOcorrenciaService tipoOcorrenciaService;
    private final FotoOcorrenciaService fotoOcorrenciaService; // Necessário para o DTO

    private final Path pastaUpload = Paths.get("uploads");

    public List<OcorrenciaDTOs.Response> listarPorPropriedade(UUID propriedadeId) {
        return ocorrenciaRepository.findByPropriedadeIdOrderByTipoOcorrenciaNome(propriedadeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OcorrenciaDTOs.Response salvarComFotos(OcorrenciaDTOs.Request dto, List<MultipartFile> arquivos) throws IOException {

        Propriedade propriedade = propriedadeService.buscarEntidadePorId(dto.propriedadeId());
        TipoOcorrencia tipoOcorrencia = tipoOcorrenciaService.buscarEntidadePorId(dto.tipoOcorrenciaId());

        List<Incidente> incidentes = new ArrayList<>();
        if (dto.incidentes() != null && !dto.incidentes().isEmpty()) {
            incidentes = dto.incidentes().stream()
                    .map(incidenteService::buscarEntidadePorId)
                    .collect(Collectors.toList());
        }

        Ocorrencia ocorrencia = mapearDtoParaEntidade(dto, propriedade, tipoOcorrencia, incidentes);

        if (arquivos != null && !arquivos.isEmpty()) {
            if (!Files.exists(pastaUpload)) {
                Files.createDirectories(pastaUpload);
            }

            List<FotoOcorrencia> fotos = arquivos.stream().map(arquivo -> {
                try {
                    String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                    Path destino = this.pastaUpload.resolve(nomeArquivo);
                    arquivo.transferTo(destino);

                    FotoOcorrencia foto = new FotoOcorrencia();
                    foto.setCaminho(nomeArquivo);
                    foto.setNome(arquivo.getOriginalFilename());
                    foto.setOcorrencia(ocorrencia);
                    return foto;
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar arquivo: " + arquivo.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());
            ocorrencia.setFotos(fotos);
        }

        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(ocorrencia);
        return toResponseDto(ocorrenciaSalva);
    }

    @Transactional
    public OcorrenciaDTOs.Response updateOcorrencia(UUID ocorrenciaId, OcorrenciaDTOs.UpdateRequest dto, List<MultipartFile> novasFotos) throws IOException {

        Ocorrencia ocorrencia = ocorrenciaRepository.findById(ocorrenciaId)
                .orElseThrow(() -> new EntityNotFoundException("Ocorrência não encontrada: " + ocorrenciaId));

        if (dto.fotosParaExcluir() != null) {
            dto.fotosParaExcluir().forEach(fotoOcorrenciaService::deleteFoto);
        }

        if (novasFotos != null && !novasFotos.isEmpty()) {
            if (!Files.exists(pastaUpload)) {
                Files.createDirectories(pastaUpload);
            }
            for (MultipartFile arquivo : novasFotos) {
                String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Path destino = this.pastaUpload.resolve(nomeArquivo);
                arquivo.transferTo(destino);

                FotoOcorrencia foto = new FotoOcorrencia();
                foto.setCaminho(nomeArquivo);
                foto.setNome(arquivo.getOriginalFilename());
                foto.setOcorrencia(ocorrencia);
                ocorrencia.getFotos().add(foto);
            }
        }

        TipoOcorrencia tipo = tipoOcorrenciaService.buscarEntidadePorId(dto.tipoOcorrenciaId());
        List<Incidente> incidentes = dto.incidentes().stream()
                .map(incidenteService::buscarEntidadePorId)
                .collect(Collectors.toList());

        ocorrencia.setTipoOcorrencia(tipo);
        ocorrencia.setIncidentes(incidentes);
        ocorrencia.setData(dto.data());
        ocorrencia.setDescricao(dto.descricao());

        Ocorrencia ocorrenciaAtualizada = ocorrenciaRepository.save(ocorrencia);
        return toResponseDto(ocorrenciaAtualizada);
    }

    @Transactional
    public void excluir(UUID id) {

        Ocorrencia ocorrencia = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ocorrência não encontrada com ID: " + id));

        if (ocorrencia.getFotos() != null && !ocorrencia.getFotos().isEmpty()) {
            for (FotoOcorrencia foto : ocorrencia.getFotos()) {
                try {
                    Path arquivoPath = pastaUpload.resolve(foto.getCaminho());
                    Files.deleteIfExists(arquivoPath);
                } catch (IOException e) {
                    System.err.println("Erro ao deletar arquivo físico: " + foto.getCaminho() + " - " + e.getMessage());
                }
            }
        }

        ocorrenciaRepository.deleteById(id);
    }

    private Ocorrencia mapearDtoParaEntidade(OcorrenciaDTOs.Request dto, Propriedade propriedade, TipoOcorrencia tipoOcorrencia, List<Incidente> incidentes) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setPropriedade(propriedade);
        ocorrencia.setTipoOcorrencia(tipoOcorrencia);
        ocorrencia.setIncidentes(incidentes);
        ocorrencia.setData(dto.data());
        ocorrencia.setDescricao(dto.descricao());
        return ocorrencia;
    }

    private OcorrenciaDTOs.Response toResponseDto(Ocorrencia ocorrencia) {
        List<LookupDTO> incidentesDto = ocorrencia.getIncidentes().stream()
                .map(i -> new LookupDTO(i.getId(), i.getNome()))
                .collect(Collectors.toList());

        List<FotoOcorrenciaDTO> fotosDto = fotoOcorrenciaService.listarPorOcorrencia(ocorrencia.getId());

        return new OcorrenciaDTOs.Response(
                ocorrencia.getId(),
                ocorrencia.getData(),
                ocorrencia.getDescricao(),
                new LookupDTO(ocorrencia.getTipoOcorrencia().getId(), ocorrencia.getTipoOcorrencia().getNome()),
                incidentesDto,
                fotosDto
        );
    }
}

