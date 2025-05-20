package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import br.ufsm.smpp.service.VulnerabilidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vulnerabilidades")
@RequiredArgsConstructor
public class VulnerabilidadeController {

    private final VulnerabilidadeService vulnerabilidadeService;

    @GetMapping
    public List<Vulnerabilidade> listarTodas() {
        return vulnerabilidadeService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vulnerabilidade> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(vulnerabilidadeService.buscarPorId(id));
    }
}
