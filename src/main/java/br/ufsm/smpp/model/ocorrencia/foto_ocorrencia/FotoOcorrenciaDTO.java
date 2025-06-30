package br.ufsm.smpp.model.ocorrencia.foto_ocorrencia;
import java.util.UUID;

/**
 * DTO para representar os dados de uma foto a serem enviados para o frontend.
 *
 * @param id   O ID único da entidade FotoOcorrencia.
 * @param nome O nome original do arquivo que foi enviado pelo usuário.
 * @param url  A URL completa e acessível para o frontend carregar a imagem.
 */
public record FotoOcorrenciaDTO(
        UUID id,
        String nome,
        String url
) {}