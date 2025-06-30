package br.ufsm.smpp.controller;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioDTOs;
import br.ufsm.smpp.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTOs.Response>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOs.Response> buscarPorId(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(usuarioService.buscarDtoPorId(id, usuarioLogado));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTOs.Response> criarUsuario(
            @RequestBody @Valid UsuarioDTOs.Create dto) {
        UsuarioDTOs.Response novoUsuario = usuarioService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTOs.Response> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioDTOs.Update dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        UsuarioDTOs.Response usuarioAtualizado = usuarioService.atualizarUsuario(id, dto, usuarioLogado);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        usuarioService.deletar(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}


