package br.ufsm.smpp.service;

import br.ufsm.smpp.dto.LookupDTO; // Adicionado para o DTO de resposta
import br.ufsm.smpp.model.Vulnerabilidade;
import br.ufsm.smpp.repository.VulnerabilidadeRepository;
import jakarta.persistence.EntityNotFoundException; // Adicionado para exceção específica
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort; // Adicionado para ordenação
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Vulnerabilidade.
 * Vulnerabilidades representam fatores de risco para uma propriedade (ex: "Suscetível a geadas", "Baixa fertilidade do solo").
 * Este serviço é usado para obter e gerenciar as vulnerabilidades.
 */
@Service
@RequiredArgsConstructor
public class VulnerabilidadeService {

    private final VulnerabilidadeRepository vulnerabilidadeRepository;

    /**
     * Lista todas as vulnerabilidades cadastradas, ordenadas alfabeticamente por nome.
     *
     * @return Uma lista de {@link LookupDTO} contendo o ID e o nome de cada vulnerabilidade,
     *         ideal para ser usada em componentes de seleção no front-end.
     */
    public List<LookupDTO> listarTodas() {
        // Retorna uma lista de DTOs ordenados, o que é uma melhor prática.
        return vulnerabilidadeRepository.findAll(Sort.by("nome")).stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Busca uma entidade {@link Vulnerabilidade} completa pelo seu ID.
     *
     * @param id O UUID da vulnerabilidade a ser buscada.
     * @return A entidade {@link Vulnerabilidade} encontrada.
     * @throws EntityNotFoundException se nenhuma vulnerabilidade for encontrada com o ID fornecido.
     */
    public Vulnerabilidade buscarPorId(UUID id) {
        // Lança uma exceção específica com uma mensagem de erro mais clara.
        return vulnerabilidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vulnerabilidade não encontrada com ID: " + id));
    }

    /**
     * Converte uma entidade {@link Vulnerabilidade} para seu DTO de apresentação {@link LookupDTO}.
     *
     * @param vulnerabilidade A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    private LookupDTO toDto(Vulnerabilidade vulnerabilidade) {
        return new LookupDTO(vulnerabilidade.getId(), vulnerabilidade.getNome());
    }
}