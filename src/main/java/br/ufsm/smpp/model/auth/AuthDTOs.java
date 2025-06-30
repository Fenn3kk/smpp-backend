package br.ufsm.smpp.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class AuthDTOs {

    private AuthDTOs() {}

    /** DTO para a requisição de login. */
    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {}

    /** DTO para a requisição de cadastro público. */
    public record RegisterRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank @Size(min = 6) String senha
    ) {}

    /** DTO para a resposta da API após um login bem-sucedido. */
    public record JwtResponse(
            String token,
            UUID usuarioId,
            String tipoUsuario,
            String nome
    ) {}
}
