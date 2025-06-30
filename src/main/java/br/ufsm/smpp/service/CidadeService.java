package br.ufsm.smpp.service;

import br.ufsm.smpp.model.BuscaDTO;
import br.ufsm.smpp.model.propriedade.cidade.Cidade;
import br.ufsm.smpp.model.propriedade.cidade.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    /**
     * Retorna uma lista de todas as cidades formatadas como DTOs.
     */
    public List<BuscaDTO> listarTodas() {
        return cidadeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Busca uma entidade Cidade pelo ID. Lança uma exceção se não for encontrada.
     * Usado internamente por outros serviços.
     */
    public Cidade buscarEntidadePorId(UUID id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com ID: " + id));
    }

    /**
     * Busca uma cidade pelo ID e a formata como um DTO para a resposta da API.
     */
    public BuscaDTO buscarDtoPorId(UUID id) {
        Cidade cidade = buscarEntidadePorId(id);
        return toDto(cidade);
    }

    /**
     * Converte uma entidade Cidade em um BuscaDTO.
     */
    private BuscaDTO toDto(Cidade cidade) {
        return new BuscaDTO(cidade.getId(), cidade.getNome());
    }
}
