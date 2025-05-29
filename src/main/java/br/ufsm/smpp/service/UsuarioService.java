package br.ufsm.smpp.service;

import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Usuario salvar(Usuario usuario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth != null && auth.isAuthenticated() &&
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Se não for admin, força o tipo para COMUM
        if (!isAdmin) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.COMUM);
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }


    public Usuario atualizar(UUID id, Usuario usuarioAtualizado) {
        Usuario existente = buscarPorId(id);

        existente.setNome(usuarioAtualizado.getNome());
        existente.setEmail(usuarioAtualizado.getEmail());
        existente.setTelefone(usuarioAtualizado.getTelefone());
        existente.setTipoUsuario(usuarioAtualizado.getTipoUsuario());

        // Atualiza senha só se ela não for nula ou vazia
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }
        // Se senha não vier ou for vazia, mantém a senha atual

        return usuarioRepository.save(existente);
    }


    public void deletar(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
