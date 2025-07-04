package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.TipoOcorrencia;
import br.ufsm.smpp.repository.TipoOcorrenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort; // Import para ordenação
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade TipoOcorrencia.
 * Tipos de Ocorrência são categorias para classificar os eventos (ex: "Geada", "Vendaval").
 * Este serviço é usado principalmente para popular listas de seleção no front-end.
 */
@Service
@RequiredArgsConstructor
public class TipoOcorrenciaService {

    private final TipoOcorrenciaRepository repository;

    /**
     * Lista todos os tipos de ocorrência cadastrados, ordenados alfabeticamente por nome.
     * A ordenação melhora a usabilidade em componentes de front-end como dropdowns.
     *
     * @return Uma lista de {@link LookupDTO}, contendo o ID e o nome de cada tipo.
     */
    public List<LookupDTO> listarTodos() {
        // Adicionar ordenação (Sort.by("nome")) é uma boa prática para listas de seleção.
        return repository.findAll(Sort.by("nome")).stream().map(this::toDto).toList();
    }

    /**
     * Busca uma entidade {@link TipoOcorrencia} completa pelo seu ID.
     * Este método é para uso interno do backend, quando a entidade completa é necessária
     * para criar associações com outras entidades.
     *
     * @param id O UUID do tipo de ocorrência a ser buscado.
     * @return A entidade {@link TipoOcorrencia} encontrada.
     * @throws EntityNotFoundException se nenhum tipo de ocorrência for encontrado com o ID fornecido.
     */
    public TipoOcorrencia buscarEntidadePorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Ocorrência não encontrado com ID: " + id));
    }

    /**
     * Busca um tipo de ocorrência pelo seu ID e o retorna como um DTO.
     * Este método é ideal para ser exposto na camada de controller.
     *
     * @param id O UUID do tipo de ocorrência a ser buscado.
     * @return O {@link LookupDTO} correspondente ao tipo de ocorrência encontrado.
     */
    public LookupDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    /**
     * Converte uma entidade {@link TipoOcorrencia} para seu DTO de apresentação {@link LookupDTO}.
     *
     * @param tipo A entidade a ser convertida.
     * @return O DTO correspondente com o ID e o nome do tipo de ocorrência.
     */
    private LookupDTO toDto(TipoOcorrencia tipo) {
        return new LookupDTO(tipo.getId(), tipo.getNome());
    }
}