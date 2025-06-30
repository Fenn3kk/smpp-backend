package br.ufsm.smpp.model.propriedade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * DTO para a requisição de criação ou atualização de uma Propriedade.
 * Usa 'record' para imutabilidade e concisão.
 */
public record PropriedadeDTO(
        @NotBlank(message = "O nome da propriedade não pode ser vazio.")
        @Size(min = 2, max = 255)
        String nome,

        @NotNull(message = "O ID da cidade é obrigatório.")
        UUID cidadeId,

        @NotBlank(message = "As coordenadas são obrigatórias.")
        String coordenadas,

        // O proprietário é opcional no DTO. Se for nulo ou vazio,
        // a lógica de serviço usará os dados do usuário logado.
        String proprietario,

        String telefoneProprietario,

        @NotNull(message = "É necessário selecionar ao menos uma atividade.")
        @Size(min = 1, message = "Selecione pelo menos uma atividade.")
        List<UUID> atividades,

        // Lista opcional de vulnerabilidades.
        List<UUID> vulnerabilidades
) {}
