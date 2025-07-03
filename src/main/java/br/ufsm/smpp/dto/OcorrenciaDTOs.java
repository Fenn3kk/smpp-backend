package br.ufsm.smpp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class OcorrenciaDTOs {

        private OcorrenciaDTOs() {}

        public record Request(
                @NotNull(message = "O ID do tipo de ocorrência é obrigatório.")
                UUID tipoOcorrenciaId,

                @NotNull(message = "A data da ocorrência é obrigatória.")
                @PastOrPresent(message = "A data da ocorrência não pode ser no futuro.")
                LocalDate data,

                @NotBlank(message = "A descrição não pode ser vazia.")
                String descricao,

                @NotNull(message = "O ID da propriedade é obrigatório.")
                UUID propriedadeId,

                List<UUID> incidentes
        ) {}

        public record UpdateRequest(
                @NotNull UUID tipoOcorrenciaId,
                @NotNull @PastOrPresent LocalDate data,
                @NotBlank String descricao,
                List<UUID> incidentes,
                List<UUID> fotosParaExcluir
        ) {}

        public record Response(
                UUID id,
                LocalDate data,
                String descricao,
                LookupDTO tipoOcorrencia,
                List<LookupDTO> incidentes,
                List<FotoOcorrenciaDTO> fotos
        ) {}
}

