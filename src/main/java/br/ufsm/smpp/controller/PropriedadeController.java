package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.service.PropriedadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeService propriedadeService;

    @GetMapping
    public List<Propriedade> listarTodas() {
        return propriedadeService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propriedade> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(propriedadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Propriedade> salvar(@RequestBody Propriedade propriedade) {
        return ResponseEntity.ok(propriedadeService.salvar(propriedade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Propriedade> atualizar(@PathVariable UUID id, @RequestBody Propriedade propriedade) {
        return ResponseEntity.ok(propriedadeService.atualizar(id, propriedade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        propriedadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

