package br.ufsm.smpp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Classe final que agrupa todos os Data Transfer Objects (DTOs) relacionados à entidade Propriedade.
 * O construtor privado impede a instanciação desta classe, que serve apenas como um contêiner
 * para os records aninhados.
 */
public final class PropriedadeDTOs {

        /**
         * Construtor privado para impedir a instanciação da classe utilitária.
         */
        private PropriedadeDTOs() {}

        /**
         * DTO para receber dados na criação ou atualização de uma propriedade.
         * Contém os dados necessários e as validações para o registro.
         *
         * @param nome Nome da propriedade. Não pode ser vazio e deve ter entre 2 e 255 caracteres.
         * @param cidadeId O ID da cidade à qual a propriedade pertence. É obrigatório.
         * @param coordenadas As coordenadas geográficas da propriedade. É obrigatório.
         * @param proprietario Nome do proprietário da terra.
         * @param telefoneProprietario Telefone de contato do proprietário.
         * @param atividades Lista de IDs das atividades econômicas realizadas na propriedade. Pelo menos uma deve ser selecionada.
         * @param vulnerabilidades Lista opcional de IDs das vulnerabilidades associadas à propriedade.
         */
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

        /**
         * DTO para enviar os dados completos de uma propriedade em respostas de API.
         *
         * @param id O identificador único da propriedade.
         * @param nome O nome da propriedade.
         * @param cidade A cidade da propriedade, representada por um {@link LookupDTO}.
         * @param coordenadas As coordenadas geográficas da propriedade.
         * @param proprietario O nome do proprietário.
         * @param telefoneProprietario O telefone do proprietário.
         * @param atividades A lista de atividades, cada uma representada por um {@link LookupDTO}.
         * @param vulnerabilidades A lista de vulnerabilidades, cada uma representada por um {@link LookupDTO}.
         * @param usuario Os dados simplificados do usuário que cadastrou a propriedade, representados por um {@link SimpleUserDTO}.
         */
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

        /**
         * DTO simplificado para representar um usuário, contendo apenas ID e nome.
         * Útil para ser embutido em outros DTOs sem expor dados desnecessários.
         *
         * @param id O identificador único do usuário.
         * @param nome O nome do usuário.
         */
        public record SimpleUserDTO(UUID id, String nome) {}
}