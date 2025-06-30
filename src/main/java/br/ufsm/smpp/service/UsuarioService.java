package br.ufsm.smpp.service;

import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioDTOs;
import br.ufsm.smpp.model.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // --- MÉTODOS PÚBLICOS (LÓGICA DE NEGÓCIO) ---

    public List<UsuarioDTOs.Response> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTOs.Response buscarDtoPorId(UUID id, Usuario usuarioLogado) {
        validarAcesso(id, usuarioLogado);
        Usuario usuario = buscarEntidadePorId(id);
        return toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioDTOs.Response criarUsuario(UsuarioDTOs.Create dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setEmail(dto.email());
        novoUsuario.setTelefone(dto.telefone());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setTipoUsuario(
                "ADMIN".equalsIgnoreCase(dto.tipoUsuario()) ? Usuario.TipoUsuario.ADMIN : Usuario.TipoUsuario.COMUM
        );

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return toResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioDTOs.Response atualizarUsuario(UUID id, UsuarioDTOs.Update dto, Usuario usuarioLogado) {
        validarAcesso(id, usuarioLogado);
        Usuario usuarioExistente = buscarEntidadePorId(id);

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setTelefone(dto.telefone());

        if (dto.novaSenha() != null && !dto.novaSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.novaSenha()));
        }

        // CORREÇÃO: Compara o enum diretamente, em vez de chamar um método inexistente.
        if (usuarioLogado.getTipoUsuario() == Usuario.TipoUsuario.ADMIN) {
            usuarioExistente.setTipoUsuario(
                    "ADMIN".equalsIgnoreCase(dto.tipoUsuario()) ? Usuario.TipoUsuario.ADMIN : Usuario.TipoUsuario.COMUM
            );
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuarioExistente);
        return toResponseDTO(usuarioSalvo);
    }

    @Transactional
    public void deletar(UUID id, Usuario usuarioLogado) {
        validarAcesso(id, usuarioLogado);
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    // --- MÉTODOS AUXILIARES (LÓGICA INTERNA) ---

    public Usuario buscarEntidadePorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }

    private void validarAcesso(UUID idDoAlvo, Usuario usuarioLogado) {
        // CORREÇÃO: Compara o enum diretamente.
        boolean ehAdmin = usuarioLogado.getTipoUsuario() == Usuario.TipoUsuario.ADMIN;
        boolean ehMesmoUsuario = usuarioLogado.getId().equals(idDoAlvo);

        if (!ehAdmin && !ehMesmoUsuario) {
            throw new AccessDeniedException("Acesso negado.");
        }
    }

    private UsuarioDTOs.Response toResponseDTO(Usuario usuario) {
        return new UsuarioDTOs.Response(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipoUsuario().name()
        );
    }
}