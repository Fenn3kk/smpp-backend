package br.ufsm.smpp.dto;
import java.util.UUID;

public record FotoOcorrenciaDTO(
        UUID id,
        String nome,
        String url
) {}