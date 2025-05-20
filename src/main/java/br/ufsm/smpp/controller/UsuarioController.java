package br.ufsm.smpp.controller;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        validarAcessoUsuario(id);
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable UUID id, @RequestBody Usuario usuario) {
        validarAcessoUsuario(id);
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        validarAcessoUsuario(id);
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarAcessoUsuario(UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario logado = (Usuario) auth.getPrincipal();

        boolean ehAdmin = logado.getTipoUsuario() == Usuario.TipoUsuario.ADMIN;
        boolean mesmoUsuario = logado.getId().equals(id);

        if (!ehAdmin && !mesmoUsuario) {
            throw new AccessDeniedException("Acesso negado");
        }
    }
}
