package br.ufsm.smpp.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Classe final que agrupa todos os Data Transfer Objects (DTOs) relacionados à entidade Usuario.
 * O construtor privado impede a instanciação desta classe, que serve apenas como um contêiner
 * para os records aninhados.
 */
public final class UsuarioDTOs {

    /**
     * Construtor privado para impedir a instanciação da classe utilitária.
     */
    private UsuarioDTOs() {}

    /**
     * DTO para a criação de um novo usuário.
     * Contém os dados necessários e as validações para o registro.
     *
     * @param nome Nome completo do usuário. Não pode ser vazio.
     * @param email Endereço de e-mail do usuário. Deve ser um formato de e-mail válido e não pode ser vazio.
     * @param telefone Número de telefone para contato. Não pode ser vazio.
     * @param senha Senha para o login. Deve ter no mínimo 6 caracteres.
     * @param tipoUsuario Opcional. Define o perfil do usuário (ex: "ADMIN"). Se não for fornecido, o serviço o definirá como 'COMUM'.
     */
    public record Create(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String senha,
            String tipoUsuario
    ) {}

    /**
     * DTO para a atualização dos dados de um usuário existente.
     *
     * @param nome Novo nome completo do usuário.
     * @param email Novo endereço de e-mail do usuário.
     * @param telefone Novo número de telefone do usuário.
     * @param novaSenha Opcional. A nova senha desejada. Se fornecida, deve ter no mínimo 6 caracteres.
     * @param tipoUsuario Opcional. O novo perfil do usuário.
     * @param senhaAtual A senha atual do usuário, necessária para confirmar a identidade antes de aplicar as alterações.
     */
    public record Update(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String novaSenha,
            String tipoUsuario,
            String senhaAtual
    ) {}

    /**
     * DTO para representar os dados de um usuário em respostas de API.
     * Contém informações públicas e seguras para serem enviadas ao cliente (exclui a senha).
     *
     * @param id O identificador único do usuário.
     * @param nome O nome do usuário.
     * @param email O e-mail do usuário.
     * @param telefone O telefone do usuário.
     * @param tipoUsuario O perfil do usuário (ex: "ADMIN", "COMUM").
     */
    public record Response(
            UUID id,
            String nome,
            String email,
            String telefone,
            String tipoUsuario
    ) {}

    /**
     * DTO de resposta para operações de atualização que também geram um novo token JWT.
     * Isso é útil quando dados sensíveis que fazem parte do token (como o e-mail) são alterados.
     *
     * @param usuario Os dados atualizados do usuário no formato {@link Response}.
     * @param token O novo token JWT gerado.
     */
    public record UpdateResponse(Response usuario, String token) {}
}