package br.ufsm.smpp.service;

import br.ufsm.smpp.model.BuscaDTO;
import br.ufsm.smpp.model.incidente.Incidente;
import br.ufsm.smpp.model.incidente.IncidenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;

    public List<BuscaDTO> listarTodos() {
        return incidenteRepository.findAll().stream().map(this::toDto).toList();
    }

    public Incidente buscarEntidadePorId(UUID id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incidente não encontrado com ID: " + id));
    }

    public BuscaDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    @Transactional
    public BuscaDTO salvar(BuscaDTO dto) {
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

    private BuscaDTO toDto(Incidente incidente) {
        return new BuscaDTO(incidente.getId(), incidente.getNome());
    }
}

