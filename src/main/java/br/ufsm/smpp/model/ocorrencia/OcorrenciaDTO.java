package br.ufsm.smpp.model.ocorrencia;

import br.ufsm.smpp.model.incidente.Incidente;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO para a requisição de criação de uma Ocorrência.
 * Usa 'record' para imutabilidade e concisão.
 */
public record OcorrenciaDTO(
        @NotNull(message = "O ID do tipo de ocorrência é obrigatório.")
        UUID tipoOcorrenciaId,

        @NotNull(message = "A data da ocorrência é obrigatória.")
        @PastOrPresent(message = "A data da ocorrência não pode ser no futuro.")
        LocalDate data,

        @NotBlank(message = "A descrição não pode ser vazia.")
        String descricao,

        @NotNull(message = "O ID da propriedade é obrigatório.")
        UUID propriedadeId,

        // Lista opcional de incidentes associados.
        List<UUID> incidentes
) {}

