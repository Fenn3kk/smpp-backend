package br.ufsm.smpp.model.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public final class UsuarioDTOs {

    private UsuarioDTOs() {}

    /**
     * DTO para a requisição de criação de um usuário.
     * Usado em: POST /usuarios
     */
    public record Create(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String senha,
            String tipoUsuario // Opcional, o serviço definirá como 'COMUM' se não for admin
    ) {}

    /**
     * DTO para a requisição de atualização de um usuário.
     * Usado em: PUT /usuarios/{id}
     */
    public record Update(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String novaSenha, // Senha é opcional
            String tipoUsuario // Apenas admins poderão alterar
    ) {}

    /**
     * DTO para a resposta da API, NUNCA expõe a senha.
     * Usado como retorno em GET, POST e PUT.
     */
    public record Response(
            UUID id,
            String nome,
            String email,
            String telefone,
            String tipoUsuario
    ) {}
}