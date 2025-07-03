package br.ufsm.smpp.service;
import br.ufsm.smpp.security.JwtUtil;
import br.ufsm.smpp.dto.AuthDTOs;
import br.ufsm.smpp.model.Usuario;
import br.ufsm.smpp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthDTOs.JwtResponse login(AuthDTOs.LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());
        return new AuthDTOs.JwtResponse(
                token,
                usuario.getId(),
                usuario.getTipoUsuario().name(),
                usuario.getNome()
        );
    }

    public void register(AuthDTOs.RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.nome());
        novoUsuario.setEmail(request.email());
        novoUsuario.setTelefone(request.telefone());
        novoUsuario.setSenha(passwordEncoder.encode(request.senha()));
        novoUsuario.setTipoUsuario(Usuario.TipoUsuario.COMUM);

        usuarioRepository.save(novoUsuario);
    }
}

