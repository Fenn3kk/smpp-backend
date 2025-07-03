package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.OcorrenciaDTOs;
import br.ufsm.smpp.model.Ocorrencia;
import br.ufsm.smpp.service.OcorrenciaService;
import br.ufsm.smpp.service.PropriedadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @GetMapping("/propriedade/{propriedadeId}")
    public ResponseEntity<List<OcorrenciaDTOs.Response>> listarPorPropriedade(@PathVariable UUID propriedadeId) {
        List<OcorrenciaDTOs.Response> ocorrencias = ocorrenciaService.listarPorPropriedade(propriedadeId);
        return ResponseEntity.ok(ocorrencias);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcorrenciaDTOs.Response> salvar(
            @RequestPart("ocorrenciaDto") @Valid OcorrenciaDTOs.Request dto,
            @RequestPart(name = "fotos", required = false) List<MultipartFile> fotos) throws IOException {

        OcorrenciaDTOs.Response ocorrenciaSalva = ocorrenciaService.salvarComFotos(dto, fotos);
        return ResponseEntity.status(HttpStatus.CREATED).body(ocorrenciaSalva);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcorrenciaDTOs.Response> atualizar(
            @PathVariable UUID id,
            @RequestPart("ocorrenciaUpdateDto") @Valid OcorrenciaDTOs.UpdateRequest dto,
            @RequestPart(name = "novasFotos", required = false) List<MultipartFile> novasFotos) throws IOException {

        OcorrenciaDTOs.Response atualizada = ocorrenciaService.updateOcorrencia(id, dto, novasFotos);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirOcorrencia(@PathVariable UUID id) {
        ocorrenciaService.excluir(id);
    }
}
