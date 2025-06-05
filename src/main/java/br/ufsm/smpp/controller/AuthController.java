package br.ufsm.smpp.controller;

import br.ufsm.smpp.infra.security.JwtUtil;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.usuario.UsuarioRepository;
import br.ufsm.smpp.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<Usuario> usuarioOpt = authService.autenticar(request.getEmail(), request.getSenha());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = jwtUtil.gerarToken(usuario.getEmail());
            return ResponseEntity.ok(new JwtLoginResponse(
                    token,
                    usuario.getId().toString(),
                    usuario.getTipoUsuario().toString() // ✅ envia tipoUsuario
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        Optional<Usuario> novoUsuario = authService.cadastrar(usuario);

        if (novoUsuario.isPresent()) {
            String token = jwtUtil.gerarToken(novoUsuario.get().getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(token));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
    }

    @Data
    static class LoginRequest {
        private String email;
        private String senha;
    }

    @Data
    @AllArgsConstructor
    public static class JwtResponse {
        private String token;
    }

    @Data
    @AllArgsConstructor
    public static class JwtLoginResponse {
        private String token;
        private String usuarioId;
        private String tipoUsuario; // ✅ novo campo
    }
}
