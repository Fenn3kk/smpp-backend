package br.ufsm.smpp.model;

import java.util.UUID;

/**
 * Um DTO genérico para respostas de entidades de cadastro simples.
 */
public record BuscaDTO(UUID id, String nome) {
}
