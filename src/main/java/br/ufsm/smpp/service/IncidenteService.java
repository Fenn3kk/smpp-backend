package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Incidente;
import br.ufsm.smpp.repository.IncidenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Incidente.
 * Incidentes representam danos ou perdas que podem ser associados a uma ocorrência
 * (ex: "Perda de lavoura", "Dano estrutural").
 * Este serviço gerencia as operações de CRUD para incidentes.
 */
@Service
@RequiredArgsConstructor
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;

    /**
     * Lista todos os incidentes cadastrados, ordenados alfabeticamente por nome.
     *
     * @return Uma lista de {@link LookupDTO}, ideal para popular listas de seleção no front-end.
     */
    public List<LookupDTO> listarTodos() {
        return incidenteRepository.findAll(Sort.by("nome")).stream().map(this::toDto).toList();
    }

    /**
     * Busca uma entidade {@link Incidente} completa pelo seu ID.
     * Este método é para uso interno do backend, quando a entidade completa é necessária.
     *
     * @param id O UUID do incidente a ser buscado.
     * @return A entidade {@link Incidente} encontrada.
     * @throws EntityNotFoundException se nenhum incidente for encontrado com o ID fornecido.
     */
    public Incidente buscarEntidadePorId(UUID id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incidente não encontrado com ID: " + id));
    }

    /**
     * Busca um incidente pelo seu ID e o retorna como um DTO.
     * Este método é exposto para a camada de controller.
     *
     * @param id O UUID do incidente a ser buscado.
     * @return O {@link LookupDTO} correspondente ao incidente encontrado.
     */
    public LookupDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    /**
     * Cria e salva um novo incidente no banco de dados.
     * A operação é transacional.
     *
     * @param dto O DTO contendo o nome do novo incidente.
     * @return O {@link LookupDTO} do incidente recém-criado, incluindo seu novo ID.
     */
    @Transactional
    public LookupDTO salvar(LookupDTO dto) {
        Incidente incidente = new Incidente();
        incidente.setNome(dto.nome());
        Incidente salvo = incidenteRepository.save(incidente);
        return toDto(salvo);
    }

    /**
     * Exclui um incidente do banco de dados pelo seu ID.
     * A operação é transacional.
     *
     * @param id O UUID do incidente a ser excluído.
     * @throws EntityNotFoundException se o incidente não for encontrado antes da exclusão.
     */
    @Transactional
    public void excluir(UUID id) {
        // Esta verificação garante que uma EntityNotFoundException seja lançada,
        // o que pode ser tratado por um ControllerAdvice para retornar um status 404.
        if (!incidenteRepository.existsById(id)) {
            throw new EntityNotFoundException("Incidente não encontrado com ID: " + id);
        }
        incidenteRepository.deleteById(id);
    }

    /**
     * Converte uma entidade {@link Incidente} para seu DTO de apresentação {@link LookupDTO}.
     *
     * @param incidente A entidade a ser convertida.
     * @return O DTO correspondente com o ID e o nome do incidente.
     */
    private LookupDTO toDto(Incidente incidente) {
        return new LookupDTO(incidente.getId(), incidente.getNome());
    }
}