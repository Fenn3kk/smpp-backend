package br.ufsm.smpp.service;
import br.ufsm.smpp.controller.AuthController;
import br.ufsm.smpp.infra.security.JwtUtil;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public Optional<Usuario> autenticar(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(email);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(senha, usuarioOpt.get().getSenha())) {
            return usuarioOpt;
        }
        return Optional.empty();
    }

    public Optional<Usuario> cadastrar(Usuario usuario) {
        if (usuarioRepo.findByEmail(usuario.getEmail()).isPresent()) {
            return Optional.empty();
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setTipoUsuario(Usuario.TipoUsuario.COMUM); // força tipo COMUM

        return Optional.of(usuarioRepo.save(usuario));
    }
}
