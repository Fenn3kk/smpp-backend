package br.ufsm.smpp.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Classe final que agrupa todos os Data Transfer Objects (DTOs) relacionados à autenticação.
 * O construtor privado impede a instanciação desta classe, que serve apenas como um contêiner
 * para os records aninhados.
 */
public final class AuthDTOs {

    /**
     * Construtor privado para impedir a instanciação da classe utilitária.
     */
    private AuthDTOs() {}

    /**
     * DTO para receber as credenciais de login de um usuário.
     *
     * @param email O e-mail do usuário. Deve ser um e-mail válido e não pode ser vazio.
     * @param senha A senha do usuário. Não pode ser vazia.
     */
    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {}

    /**
     * DTO para receber os dados de registro de um novo usuário.
     *
     * @param nome O nome completo do usuário. Não pode ser vazio.
     * @param email O e-mail do usuário. Deve ser um e-mail válido e não pode ser vazio.
     * @param telefone O número de telefone do usuário. Não pode ser vazio.
     * @param senha A senha para o novo usuário. Deve ter no mínimo 6 caracteres.
     */
    public record RegisterRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank @Size(min = 6) String senha
    ) {}

    /**
     * DTO de resposta enviado ao cliente após uma autenticação bem-sucedida.
     * Contém o token de acesso e informações básicas do usuário.
     *
     * @param token O token JWT (JSON Web Token) gerado para autorizar requisições futuras.
     * @param usuarioId O identificador único do usuário autenticado.
     * @param tipoUsuario O perfil do usuário (ex: "ADMIN", "COMUM") para controle de acesso no front-end.
     * @param nome O nome do usuário, útil para exibição na interface.
     */
    public record JwtResponse(
            String token,
            UUID usuarioId,
            String tipoUsuario,
            String nome
    ) {}
}