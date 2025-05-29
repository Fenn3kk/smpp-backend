package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.model.propriedade.PropriedadeDTO;
import br.ufsm.smpp.service.PropriedadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeService propriedadeService;

    @GetMapping
    public List<Propriedade> listarDoUsuario(@AuthenticationPrincipal(expression = "id") UUID usuarioId) {
        return propriedadeService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propriedade> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(propriedadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody PropriedadeDTO dto) {
        Propriedade propriedade = propriedadeService.fromDTO(dto);
        Propriedade salva = propriedadeService.salvar(propriedade);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @RequestBody PropriedadeDTO dto) {
        Propriedade propriedade = propriedadeService.fromDTO(dto);
        Propriedade atualizada = propriedadeService.atualizar(id, propriedade);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        propriedadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

