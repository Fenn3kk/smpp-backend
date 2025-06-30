package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.BuscaDTO;
import br.ufsm.smpp.model.ocorrencia.tipo_ocorrencia.TipoOcorrencia;
import br.ufsm.smpp.service.TipoOcorrenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tipo-ocorrencia")
@RequiredArgsConstructor
public class TipoOcorrenciaController {

    private final TipoOcorrenciaService service;

    @GetMapping
    public ResponseEntity<List<BuscaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarDtoPorId(id));
    }
}
