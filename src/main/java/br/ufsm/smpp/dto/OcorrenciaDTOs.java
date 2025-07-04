package br.ufsm.smpp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Classe final que agrupa todos os Data Transfer Objects (DTOs) relacionados à entidade Ocorrencia.
 * O construtor privado impede a instanciação desta classe, que serve apenas como um contêiner
 * para os records aninhados.
 */
public final class OcorrenciaDTOs {

        /**
         * Construtor privado para impedir a instanciação da classe utilitária.
         */
        private OcorrenciaDTOs() {}

        /**
         * DTO para receber os dados na criação de uma nova ocorrência.
         * Contém os dados e validações necessários para o registro.
         *
         * @param tipoOcorrenciaId O ID do tipo de ocorrência (ex: Alagamento, Seca). É obrigatório.
         * @param data A data em que a ocorrência aconteceu. Deve ser no passado ou presente.
         * @param descricao Uma descrição textual do que aconteceu. Não pode ser vazia.
         * @param propriedadeId O ID da propriedade onde a ocorrência foi registrada. É obrigatório.
         * @param incidentes Lista opcional de IDs dos incidentes (danos) resultantes da ocorrência.
         */
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

        /**
         * DTO para receber os dados na atualização de uma ocorrência existente.
         *
         * @param tipoOcorrenciaId O novo ID do tipo de ocorrência.
         * @param data A nova data da ocorrência.
         * @param descricao A nova descrição da ocorrência.
         * @param incidentes A nova lista de IDs de incidentes associados.
         * @param fotosParaExcluir Lista opcional de IDs de fotos que devem ser desvinculadas e excluídas desta ocorrência.
         */
        public record UpdateRequest(
                @NotNull UUID tipoOcorrenciaId,
                @NotNull @PastOrPresent LocalDate data,
                @NotBlank String descricao,
                List<UUID> incidentes,
                List<UUID> fotosParaExcluir
        ) {}

        /**
         * DTO para enviar os dados de uma ocorrência em respostas de API.
         *
         * @param id O identificador único da ocorrência.
         * @param data A data da ocorrência.
         * @param descricao A descrição detalhada.
         * @param tipoOcorrencia O tipo da ocorrência, representado por um {@link LookupDTO}.
         * @param incidentes A lista de incidentes (danos), cada um representado por um {@link LookupDTO}.
         * @param fotos A lista de fotos associadas, cada uma representada por um {@link FotoOcorrenciaDTO}.
         */
        public record Response(
                UUID id,
                LocalDate data,
                String descricao,
                LookupDTO tipoOcorrencia,
                List<LookupDTO> incidentes,
                List<FotoOcorrenciaDTO> fotos
        ) {}
}