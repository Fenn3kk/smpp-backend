package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.FotoOcorrenciaDTO;
import br.ufsm.smpp.service.FileStorageService;
import br.ufsm.smpp.service.FotoOcorrenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fotos") // A rota para obter a lista de fotos continua a mesma
@RequiredArgsConstructor
public class FotoOcorrenciaController {

    private final FotoOcorrenciaService fotoOcorrenciaService;

    @GetMapping("/ocorrencia/{ocorrenciaId}")
    public ResponseEntity<List<FotoOcorrenciaDTO>> listarFotosPorOcorrencia(@PathVariable UUID ocorrenciaId) {
        return ResponseEntity.ok(fotoOcorrenciaService.listarPorOcorrencia(ocorrenciaId));
    }

    @DeleteMapping("/{fotoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletarFoto(@PathVariable UUID fotoId) {
        fotoOcorrenciaService.deleteFoto(fotoId);
        return ResponseEntity.noContent().build();
    }
}
