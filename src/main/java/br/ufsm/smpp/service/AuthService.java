package br.ufsm.smpp.service;
import br.ufsm.smpp.controller.AuthController;
import br.ufsm.smpp.infra.security.JwtUtil;
import br.ufsm.smpp.model.auth.AuthDTOs;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // O serviço agora depende do JwtUtil

    public AuthDTOs.JwtResponse login(AuthDTOs.LoginRequest request) {
        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas."));

        // Verifica se a senha corresponde
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        // Se tudo estiver correto, gera o token
        String token = jwtUtil.gerarToken(usuario.getEmail());

        // Retorna o DTO de resposta completo
        return new AuthDTOs.JwtResponse(
                token,
                usuario.getId(),
                usuario.getTipoUsuario().name(),
                usuario.getNome()
        );
    }

    public void register(AuthDTOs.RegisterRequest request) {
        // Verifica se o e-mail já está em uso
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.nome());
        novoUsuario.setEmail(request.email());
        novoUsuario.setTelefone(request.telefone());
        novoUsuario.setSenha(passwordEncoder.encode(request.senha()));
        novoUsuario.setTipoUsuario(Usuario.TipoUsuario.COMUM); // Força o tipo COMUM no cadastro público

        usuarioRepository.save(novoUsuario);
    }
}

