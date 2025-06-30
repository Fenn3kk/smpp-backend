package br.ufsm.smpp.service;

import br.ufsm.smpp.controller.FotoOcorrenciaController;
import br.ufsm.smpp.model.ocorrencia.foto_ocorrencia.FotoOcorrencia;
import br.ufsm.smpp.model.ocorrencia.foto_ocorrencia.FotoOcorrenciaDTO;
import br.ufsm.smpp.model.ocorrencia.foto_ocorrencia.FotoOcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FotoOcorrenciaService {

    private final FotoOcorrenciaRepository fotoOcorrenciaRepository;

    public List<FotoOcorrenciaDTO> listarPorOcorrencia(UUID ocorrenciaId) {
        return fotoOcorrenciaRepository.findByOcorrenciaId(ocorrenciaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private FotoOcorrenciaDTO toDto(FotoOcorrencia foto) {
        String url = MvcUriComponentsBuilder.fromMethodName(
                FotoOcorrenciaController.class,
                "obterFoto", // Nome do método no controller
                foto.getCaminho() // Argumento do método (nomeArquivo)
        ).build().toUriString();

        return new FotoOcorrenciaDTO(foto.getId(), foto.getNome(), url);
    }
}
