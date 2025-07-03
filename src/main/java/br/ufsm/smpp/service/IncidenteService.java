package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Incidente;
import br.ufsm.smpp.repository.IncidenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;

    public List<LookupDTO> listarTodos() {
        return incidenteRepository.findAll().stream().map(this::toDto).toList();
    }

    public Incidente buscarEntidadePorId(UUID id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incidente não encontrado com ID: " + id));
    }

    public LookupDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    @Transactional
    public LookupDTO salvar(LookupDTO dto) {
        Incidente incidente = new Incidente();
        incidente.setNome(dto.nome());
        return toDto(incidenteRepository.save(incidente));
    }

    @Transactional
    public void excluir(UUID id) {
        if (!incidenteRepository.existsById(id)) {
            throw new EntityNotFoundException("Incidente não encontrado com ID: " + id);
        }
        incidenteRepository.deleteById(id);
    }

    private LookupDTO toDto(Incidente incidente) {
        return new LookupDTO(incidente.getId(), incidente.getNome());
    }
}

