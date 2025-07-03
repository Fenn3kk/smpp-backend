package br.ufsm.smpp.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class AuthDTOs {

    private AuthDTOs() {}

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {}

    public record RegisterRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank @Size(min = 6) String senha
    ) {}

    public record JwtResponse(
            String token,
            UUID usuarioId,
            String tipoUsuario,
            String nome
    ) {}
}
