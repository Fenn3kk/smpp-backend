package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.model.propriedade.PropriedadeDTO;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.service.PropriedadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeService propriedadeService;

    @GetMapping
    public ResponseEntity<List<Propriedade>> listarPorUsuario(@AuthenticationPrincipal Usuario usuario) {
        // Passa o objeto usuário inteiro, caso o serviço precise de mais informações no futuro.
        List<Propriedade> propriedades = propriedadeService.listarPorUsuario(usuario.getId());
        return ResponseEntity.ok(propriedades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Propriedade> buscarPorId(@PathVariable UUID id) {
        // O serviço agora lança uma exceção se não encontrar, simplificando o controller.
        Propriedade propriedade = propriedadeService.buscarPorId(id);
        return ResponseEntity.ok(propriedade);
    }

    @PostMapping
    public ResponseEntity<Propriedade> salvar(
            @RequestBody PropriedadeDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        // O controller apenas delega a criação para o serviço.
        Propriedade propriedadeSalva = propriedadeService.criarPropriedade(dto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(propriedadeSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Propriedade> atualizar(
            @PathVariable UUID id,
            @RequestBody PropriedadeDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        // O controller apenas delega a atualização para o serviço.
        Propriedade propriedadeAtualizada = propriedadeService.atualizarPropriedade(id, dto, usuario);
        return ResponseEntity.ok(propriedadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuario) {
        propriedadeService.deletar(id, usuario); // Passa o usuário para verificação de permissão
        return ResponseEntity.noContent().build();
    }
}

