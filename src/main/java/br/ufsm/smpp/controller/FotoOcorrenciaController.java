package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.ocorrencia.foto_ocorrencia.FotoOcorrencia;
import br.ufsm.smpp.model.ocorrencia.foto_ocorrencia.FotoOcorrenciaDTO;
import br.ufsm.smpp.service.ArquivosService;
import br.ufsm.smpp.service.FotoOcorrenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fotos") // Rota mais genérica
@RequiredArgsConstructor
public class FotoOcorrenciaController {

    private final FotoOcorrenciaService fotoOcorrenciaService;
    private final ArquivosService fileStorageService;

    @GetMapping("/ocorrencia/{ocorrenciaId}")
    public ResponseEntity<List<FotoOcorrenciaDTO>> listarFotosPorOcorrencia(@PathVariable UUID ocorrenciaId) {
        return ResponseEntity.ok(fotoOcorrenciaService.listarPorOcorrencia(ocorrenciaId));
    }

    @GetMapping("/{nomeArquivo:.+}") // Permite pontos no nome do arquivo
    public ResponseEntity<Resource> obterFoto(@PathVariable String nomeArquivo) throws IOException {
        Resource recurso = fileStorageService.loadAsResource(nomeArquivo);
        String contentType = fileStorageService.probeContentType(recurso.getFile().toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
}
