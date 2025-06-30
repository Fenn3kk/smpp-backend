package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.BuscaDTO;
import br.ufsm.smpp.model.atividade.Atividade;
import br.ufsm.smpp.service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    @GetMapping
    public ResponseEntity<List<BuscaDTO>> listarTodas() {
        return ResponseEntity.ok(atividadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscaDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(atividadeService.buscarDtoPorId(id));
    }
}