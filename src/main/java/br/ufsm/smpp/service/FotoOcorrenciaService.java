package br.ufsm.smpp.service;

import br.ufsm.smpp.model.FotoOcorrencia;
import br.ufsm.smpp.dto.FotoOcorrenciaDTO;
import br.ufsm.smpp.repository.FotoOcorrenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada às fotos de ocorrências.
 * Gerencia a listagem e a exclusão de fotos, garantindo a consistência entre
 * o banco de dados e o sistema de arquivos.
 */
@Service
@RequiredArgsConstructor
public class FotoOcorrenciaService {

    private final FotoOcorrenciaRepository fotoOcorrenciaRepository;
    private final FileStorageService fileStorageService;

    /**
     * Lista todas as fotos associadas a uma ocorrência específica.
     *
     * @param ocorrenciaId O UUID da ocorrência pai.
     * @return Uma lista de {@link FotoOcorrenciaDTO} contendo os metadados e a URL de acesso para cada foto.
     */
    public List<FotoOcorrenciaDTO> listarPorOcorrencia(UUID ocorrenciaId) {
        return fotoOcorrenciaRepository.findByOcorrenciaId(ocorrenciaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Deleta uma foto específica, removendo tanto o registro do banco de dados
     * quanto o arquivo físico do sistema de armazenamento.
     * A operação é transacional para garantir a atomicidade.
     *
     * @param fotoId O UUID da foto a ser deletada.
     * @throws EntityNotFoundException se nenhuma foto for encontrada com o ID fornecido.
     */
    @Transactional
    public void deleteFoto(UUID fotoId) {
        FotoOcorrencia foto = fotoOcorrenciaRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada com ID: " + fotoId));

        // Deleta o arquivo físico usando o serviço de armazenamento
        fileStorageService.delete(foto.getCaminho());
        // Deleta o registro do banco de dados
        fotoOcorrenciaRepository.delete(foto);
    }

    /**
     * Converte uma entidade {@link FotoOcorrencia} para seu DTO de resposta {@link FotoOcorrenciaDTO}.
     * Este método é responsável por construir a URL pública para acessar a imagem.
     *
     * @param foto A entidade a ser convertida.
     * @return O DTO correspondente com a URL de acesso.
     */
    private FotoOcorrenciaDTO toDto(FotoOcorrencia foto) {
        String urlRelativa = "/uploads/" + foto.getCaminho();

        return new FotoOcorrenciaDTO(foto.getId(), foto.getNome(), urlRelativa);
    }
}