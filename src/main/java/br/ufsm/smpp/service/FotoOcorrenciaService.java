package br.ufsm.smpp.service;
import br.ufsm.smpp.controller.FotoOcorrenciaController;
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

@Service
@RequiredArgsConstructor
public class FotoOcorrenciaService {

    private final FotoOcorrenciaRepository fotoOcorrenciaRepository;
    private final FileStorageService fileStorageService;

    public List<FotoOcorrenciaDTO> listarPorOcorrencia(UUID ocorrenciaId) {
        return fotoOcorrenciaRepository.findByOcorrenciaId(ocorrenciaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public void deleteFoto(UUID fotoId) {
        FotoOcorrencia foto = fotoOcorrenciaRepository.findById(fotoId)
                .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada com ID: " + fotoId));

        fileStorageService.delete(foto.getCaminho());
        fotoOcorrenciaRepository.delete(foto);
    }

    private FotoOcorrenciaDTO toDto(FotoOcorrencia foto) {
        String urlRelativa = "/uploads/" + foto.getCaminho();

        return new FotoOcorrenciaDTO(foto.getId(), foto.getNome(), urlRelativa);
    }
}


