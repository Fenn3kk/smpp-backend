package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Atividade;
import br.ufsm.smpp.repository.AtividadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Atividade.
 * Atividades representam os tipos de exploração de uma propriedade (ex: "Soja", "Pecuária").
 * Este serviço é usado principalmente para popular listas de seleção no front-end.
 */
@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    /**
     * Lista todas as atividades cadastradas no sistema.
     *
     * @return Uma lista de {@link LookupDTO}, contendo o ID e o nome de cada atividade,
     *         ideal para ser usada em componentes de seleção (dropdowns, etc.).
     */
    public List<LookupDTO> listarTodas() {
        return atividadeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Busca uma entidade {@link Atividade} completa pelo seu ID.
     * Este método é para uso interno do backend, quando a entidade completa é necessária.
     *
     * @param id O UUID da atividade a ser buscada.
     * @return A entidade {@link Atividade} encontrada.
     * @throws EntityNotFoundException se nenhuma atividade for encontrada com o ID fornecido.
     */
    public Atividade buscarEntidadePorId(UUID id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com ID: " + id));
    }

    /**
     * Busca uma atividade pelo seu ID e a retorna como um DTO.
     * Este método é exposto para a camada de controller.
     *
     * @param id O UUID da atividade a ser buscada.
     * @return O {@link LookupDTO} correspondente à atividade encontrada.
     */
    public LookupDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    /**
     * Converte uma entidade {@link Atividade} para seu DTO de apresentação {@link LookupDTO}.
     *
     * @param atividade A entidade a ser convertida.
     * @return O DTO correspondente com o ID e o nome da atividade.
     */
    private LookupDTO toDto(Atividade atividade) {
        return new LookupDTO(atividade.getId(), atividade.getNome());
    }
}