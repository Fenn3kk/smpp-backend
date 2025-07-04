package br.ufsm.smpp.service;
import br.ufsm.smpp.security.JwtUtil;
import br.ufsm.smpp.dto.AuthDTOs;
import br.ufsm.smpp.model.Usuario;
import br.ufsm.smpp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de negócio de autenticação e registro de usuários.
 * Centraliza as operações de login e criação de novas contas de usuário.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Autentica um usuário com base em suas credenciais (e-mail e senha).
     * Se a autenticação for bem-sucedida, gera e retorna um token JWT.
     *
     * @param request O DTO {@link AuthDTOs.LoginRequest} contendo o e-mail e a senha do usuário.
     * @return Um {@link AuthDTOs.JwtResponse} com o token JWT e informações básicas do usuário.
     * @throws BadCredentialsException se o e-mail não for encontrado ou a senha for inválida.
     *         A mensagem de erro é genérica para evitar a enumeração de usuários.
     */
    public AuthDTOs.JwtResponse login(AuthDTOs.LoginRequest request) {
        // Busca o usuário pelo e-mail. Lança exceção se não encontrar.
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas."));

        // Compara a senha fornecida com a senha armazenada (hash) no banco de dados.
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        // Gera o token JWT para o usuário autenticado.
        String token = jwtUtil.gerarToken(usuario.getEmail());

        // Retorna a resposta com o token e os dados do usuário.
        return new AuthDTOs.JwtResponse(
                token,
                usuario.getId(),
                usuario.getTipoUsuario().name(),
                usuario.getNome()
        );
    }

    /**
     * Registra um novo usuário no sistema.
     * Verifica se o e-mail já está em uso antes de criar a nova conta.
     *
     * @param request O DTO {@link AuthDTOs.RegisterRequest} com os dados do novo usuário.
     * @throws IllegalArgumentException se o e-mail fornecido já estiver cadastrado.
     */
    public void register(AuthDTOs.RegisterRequest request) {
        // Verifica se já existe um usuário com o e-mail informado.
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        // Cria uma nova instância de Usuário.
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.nome());
        novoUsuario.setEmail(request.email());
        novoUsuario.setTelefone(request.telefone());
        // Codifica a senha antes de salvá-la no banco de dados.
        novoUsuario.setSenha(passwordEncoder.encode(request.senha()));
        // Define o tipo de usuário padrão como 'COMUM'.
        novoUsuario.setTipoUsuario(Usuario.TipoUsuario.COMUM);

        // Salva o novo usuário no repositório.
        usuarioRepository.save(novoUsuario);
    }
}