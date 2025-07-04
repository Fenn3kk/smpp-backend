package br.ufsm.smpp.dto;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para representar uma foto de uma ocorrência.
 * Contém os dados essenciais para exibir a foto no cliente, incluindo
 * uma URL de acesso direto à imagem.
 *
 * @param id O identificador único do registro da foto no banco de dados.
 * @param nome O nome original do arquivo da foto (ex: "imagem_do_local.jpg").
 * @param url A URL completa e pública para acessar e exibir a imagem.
 */
public record FotoOcorrenciaDTO(
        UUID id,
        String nome,
        String url
) {}