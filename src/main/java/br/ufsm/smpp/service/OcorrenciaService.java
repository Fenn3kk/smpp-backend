package br.ufsm.smpp.service;

import br.ufsm.smpp.model.incidente.Incidente;
import br.ufsm.smpp.model.ocorrencia.Ocorrencia;
import br.ufsm.smpp.model.ocorrencia.OcorrenciaDTO;
import br.ufsm.smpp.model.ocorrencia.OcorrenciaRepository;
import br.ufsm.smpp.model.ocorrencia.foto_ocorrencia.FotoOcorrencia;
import br.ufsm.smpp.model.ocorrencia.tipo_ocorrencia.TipoOcorrencia;
import br.ufsm.smpp.model.propriedade.Propriedade;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final PropriedadeService propriedadeService;
    private final IncidenteService incidenteService;
    private final TipoOcorrenciaService tipoOcorrenciaService;
    private final Path pastaUpload = Paths.get("uploads");

    public List<Ocorrencia> listarPorPropriedade(UUID propriedadeId) {
        return ocorrenciaRepository.findByPropriedadeId(propriedadeId);
    }

    public Optional<Ocorrencia> buscarPorId(UUID id) {
        return ocorrenciaRepository.findById(id);
    }

    @Transactional
    public Ocorrencia salvar(OcorrenciaDTO dto, List<MultipartFile> arquivos) throws IOException {

        // 1. Busca as entidades relacionadas a partir dos IDs do DTO
        Propriedade propriedade = propriedadeService.buscarPorId(dto.getPropriedadeId());

        TipoOcorrencia tipoOcorrencia = tipoOcorrenciaService.buscarPorId(dto.getTipoOcorrenciaId())
                .orElseThrow(() -> new RuntimeException("Tipo de ocorrência não encontrado"));

        List<Incidente> incidentes = new ArrayList<>();
        if (dto.getIncidentes() != null && !dto.getIncidentes().isEmpty()) {
            incidentes = dto.getIncidentes().stream()
                    .map(id -> incidenteService.buscarPorId(id)
                            .orElseThrow(() -> new RuntimeException("Incidente não encontrado: " + id)))
                    .toList();
        }

        // 2. Usa o método de mapeamento para criar a entidade Ocorrencia
        Ocorrencia ocorrencia = DtoParaEntidade(dto, propriedade, tipoOcorrencia, incidentes);

        // 3. Processa e associa as fotos, se existirem
        if (arquivos != null && !arquivos.isEmpty()) {
            if (!Files.exists(pastaUpload)) {
                Files.createDirectories(pastaUpload);
            }

            List<FotoOcorrencia> fotos = new ArrayList<>();
            for (MultipartFile arquivo : arquivos) {
                // Gera um nome único para o arquivo
                String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Path destino = this.pastaUpload.resolve(nomeArquivo);

                // Salva o arquivo no disco
                arquivo.transferTo(destino);

                // Cria a entidade FotoOcorrencia
                FotoOcorrencia foto = new FotoOcorrencia();
                foto.setCaminho(nomeArquivo);
                foto.setNome(arquivo.getOriginalFilename());

                // Define a relação bidirecional
                foto.setOcorrencia(ocorrencia);
                fotos.add(foto);
            }
            ocorrencia.setFotos(fotos);
        }

        // 4. Salva a ocorrência (o CascadeType.ALL cuidará de salvar as fotos associadas)
        return ocorrenciaRepository.save(ocorrencia);
    }

    public Ocorrencia salvar(Ocorrencia ocorrencia) {
        return ocorrenciaRepository.save(ocorrencia);
    }

    private Ocorrencia DtoParaEntidade(OcorrenciaDTO dto, Propriedade propriedade, TipoOcorrencia tipoOcorrencia, List<Incidente> incidentes) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setPropriedade(propriedade);
        ocorrencia.setTipoOcorrencia(tipoOcorrencia);
        ocorrencia.setIncidentes(incidentes);
        ocorrencia.setData(dto.getData());
        ocorrencia.setDescricao(dto.getDescricao());
        return ocorrencia;
    }

    public void excluir(UUID id) {
        ocorrenciaRepository.deleteById(id);
    }
}
