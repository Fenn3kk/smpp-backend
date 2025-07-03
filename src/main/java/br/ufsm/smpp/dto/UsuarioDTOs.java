package br.ufsm.smpp.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class UsuarioDTOs {

    private UsuarioDTOs() {}

    public record Create(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String senha,
            String tipoUsuario // Opcional, o serviço definirá como 'COMUM' se não for admin
    ) {}

    public record Update(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank String telefone,
            @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String novaSenha,
            String tipoUsuario,
            String senhaAtual
    ) {}

    public record Response(
            UUID id,
            String nome,
            String email,
            String telefone,
            String tipoUsuario
    ) {}
}