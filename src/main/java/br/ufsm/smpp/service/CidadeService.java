package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Cidade;
import br.ufsm.smpp.repository.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort; // Import para ordenação
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Cidade.
 * É utilizado principalmente para obter a lista de cidades para preenchimento
 * de formulários no front-end, como no cadastro de propriedades.
 */
@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    /**
     * Lista todas as cidades cadastradas, ordenadas alfabeticamente por nome.
     *
     * @return Uma lista de {@link LookupDTO}, contendo o ID e o nome de cada cidade,
     *         ideal para ser usada em componentes de seleção (dropdowns).
     */
    public List<LookupDTO> listarTodas() {
        return cidadeRepository.findAll(Sort.by("nome")).stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Busca uma entidade {@link Cidade} completa pelo seu ID.
     * Este método é destinado ao uso interno por outros serviços que precisam
     * da entidade completa para criar associações.
     *
     * @param id O UUID da cidade a ser buscada.
     * @return A entidade {@link Cidade} encontrada.
     * @throws EntityNotFoundException se nenhuma cidade for encontrada com o ID fornecido.
     */
    public Cidade buscarEntidadePorId(UUID id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com ID: " + id));
    }

    /**
     * Busca uma cidade pelo seu ID e a retorna como um DTO.
     * Este método é ideal para ser exposto na camada de controller.
     *
     * @param id O UUID da cidade a ser buscada.
     * @return O {@link LookupDTO} correspondente à cidade encontrada.
     */
    public LookupDTO buscarDtoPorId(UUID id) {
        Cidade cidade = buscarEntidadePorId(id);
        return toDto(cidade);
    }

    /**
     * Converte uma entidade {@link Cidade} para seu DTO de apresentação {@link LookupDTO}.
     *
     * @param cidade A entidade a ser convertida.
     * @return O DTO correspondente com o ID e o nome da cidade.
     */
    private LookupDTO toDto(Cidade cidade) {
        return new LookupDTO(cidade.getId(), cidade.getNome());
    }
}