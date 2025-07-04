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

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Ocorrencia.
 * Gerencia a criação, leitura, atualização e exclusão (CRUD) de ocorrências,
 * incluindo o tratamento de upload e exclusão de arquivos de fotos associados.
 */
@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    // Injeção dos repositórios e serviços necessários.
    private final OcorrenciaRepository ocorrenciaRepository;
    private final PropriedadeService propriedadeService;
    private final IncidenteService incidenteService;
    private final TipoOcorrenciaService tipoOcorrenciaService;
    private final FotoOcorrenciaService fotoOcorrenciaService;

    /**
     * O caminho para a pasta onde os arquivos de upload serão armazenados.
     * Este caminho está fixo no código, mas em um cenário de produção,
     * seria ideal configurá-lo no 'application.properties'.
     */
    private final Path pastaUpload = Paths.get("uploads");

    /**
     * Lista todas as ocorrências de uma determinada propriedade, ordenadas pelo nome do tipo de ocorrência.
     * @param propriedadeId O UUID da propriedade.
     * @return Uma lista de {@link OcorrenciaDTOs.Response}.
     */
    public List<OcorrenciaDTOs.Response> listarPorPropriedade(UUID propriedadeId) {
        return ocorrenciaRepository.findByPropriedadeIdOrderByTipoOcorrenciaNome(propriedadeId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Salva uma nova ocorrência e suas fotos associadas.
     * A operação é transacional, garantindo que a ocorrência e os arquivos sejam salvos juntos.
     * @param dto O DTO com os dados da nova ocorrência.
     * @param arquivos A lista de arquivos de imagem a serem salvos.
     * @return O {@link OcorrenciaDTOs.Response} da ocorrência recém-criada.
     * @throws IOException se ocorrer um erro durante o salvamento dos arquivos.
     */
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

        // Processa e salva os arquivos de imagem, se houver
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
                    // Relança como uma exceção não verificada para interromper a transação
                    throw new RuntimeException("Erro ao salvar arquivo: " + arquivo.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());
            ocorrencia.setFotos(fotos);
        }

        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(ocorrencia);
        return toResponseDto(ocorrenciaSalva);
    }

    /**
     * Atualiza uma ocorrência existente, permitindo adicionar novas fotos e remover as antigas.
     * A operação é transacional.
     * @param ocorrenciaId O UUID da ocorrência a ser atualizada.
     * @param dto O DTO com os dados de atualização.
     * @param novasFotos A lista de novos arquivos de imagem a serem adicionados.
     * @return O {@link OcorrenciaDTOs.Response} da ocorrência atualizada.
     * @throws IOException se ocorrer um erro durante o salvamento dos novos arquivos.
     * @throws EntityNotFoundException se a ocorrência não for encontrada.
     */
    @Transactional
    public OcorrenciaDTOs.Response updateOcorrencia(UUID ocorrenciaId, OcorrenciaDTOs.UpdateRequest dto, List<MultipartFile> novasFotos) throws IOException {

        Ocorrencia ocorrencia = ocorrenciaRepository.findById(ocorrenciaId)
                .orElseThrow(() -> new EntityNotFoundException("Ocorrência não encontrada: " + ocorrenciaId));

        // Exclui fotos marcadas para remoção
        if (dto.fotosParaExcluir() != null) {
            dto.fotosParaExcluir().forEach(fotoOcorrenciaService::deleteFoto);
        }

        // Adiciona novas fotos, se houver
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

        // Atualiza os dados da ocorrência
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

    /**
     * Exclui uma ocorrência e todos os seus arquivos de foto associados do sistema de arquivos.
     * A operação é transacional.
     * @param id O UUID da ocorrência a ser excluída.
     * @throws EntityNotFoundException se a ocorrência não for encontrada.
     */
    @Transactional
    public void excluir(UUID id) {

        Ocorrencia ocorrencia = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ocorrência não encontrada com ID: " + id));

        // Deleta os arquivos físicos associados à ocorrência
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

        // A exclusão da ocorrência removerá as FotoOcorrencia em cascata (se configurado)
        ocorrenciaRepository.deleteById(id);
    }

    /**
     * Método auxiliar para mapear os dados de um DTO de requisição para uma entidade Ocorrencia.
     * @param dto O DTO de origem dos dados.
     * @param propriedade A entidade Propriedade associada.
     * @param tipoOcorrencia A entidade TipoOcorrencia associada.
     * @param incidentes A lista de entidades Incidente associadas.
     * @return A entidade {@link Ocorrencia} preenchida.
     */
    private Ocorrencia mapearDtoParaEntidade(OcorrenciaDTOs.Request dto, Propriedade propriedade, TipoOcorrencia tipoOcorrencia, List<Incidente> incidentes) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setPropriedade(propriedade);
        ocorrencia.setTipoOcorrencia(tipoOcorrencia);
        ocorrencia.setIncidentes(incidentes);
        ocorrencia.setData(dto.data());
        ocorrencia.setDescricao(dto.descricao());
        return ocorrencia;
    }

    /**
     * Método auxiliar para converter uma entidade {@link Ocorrencia} para seu DTO de resposta.
     * @param ocorrencia A entidade a ser convertida.
     * @return O DTO de resposta {@link OcorrenciaDTOs.Response}.
     */
    private OcorrenciaDTOs.Response toResponseDto(Ocorrencia ocorrencia) {
        List<LookupDTO> incidentesDto = ocorrencia.getIncidentes().stream()
                .map(i -> new LookupDTO(i.getId(), i.getNome()))
                .collect(Collectors.toList());

        // Reutiliza o serviço de fotos para obter os DTOs, garantindo que as URLs estejam corretas
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