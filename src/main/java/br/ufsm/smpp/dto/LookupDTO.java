package br.ufsm.smpp.dto;
import java.util.UUID;

/**
 * Um Data Transfer Object (DTO) genérico para representar entidades de forma simplificada.
 * É ideal para popular listas, menus suspensos (dropdowns) ou qualquer outro lugar
 * onde apenas o identificador e um nome de exibição são necessários.
 *
 * @param id O identificador único (UUID) da entidade.
 * @param nome O nome de exibição ou descritivo da entidade.
 */
public record LookupDTO(UUID id, String nome) {
}