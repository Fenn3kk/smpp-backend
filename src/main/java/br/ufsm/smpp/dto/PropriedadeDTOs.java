package br.ufsm.smpp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public final class PropriedadeDTOs {

        private PropriedadeDTOs() {}

        public record Request(
                @NotBlank(message = "O nome não pode ser vazio.")
                @Size(min = 2, max = 255)
                String nome,

                @NotNull(message = "O ID da cidade é obrigatório.")
                UUID cidadeId,

                @NotBlank(message = "As coordenadas são obrigatórias.")
                String coordenadas,

                String proprietario,
                String telefoneProprietario,

                @NotNull @Size(min = 1, message = "Selecione pelo menos uma atividade.")
                List<UUID> atividades,

                List<UUID> vulnerabilidades // Opcional
        ) {}

        public record Response(
                UUID id,
                String nome,
                LookupDTO cidade,
                String coordenadas,
                String proprietario,
                String telefoneProprietario,
                List<LookupDTO> atividades,
                List<LookupDTO> vulnerabilidades,
                SimpleUserDTO usuario // DTO simples para o usuário que cadastrou
        ) {}

        public record SimpleUserDTO(UUID id, String nome) {}
}