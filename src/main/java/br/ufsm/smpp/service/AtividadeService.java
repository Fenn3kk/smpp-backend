package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Atividade;
import br.ufsm.smpp.repository.AtividadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public List<LookupDTO> listarTodas() {
        return atividadeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Atividade buscarEntidadePorId(UUID id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com ID: " + id));
    }

    public LookupDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    private LookupDTO toDto(Atividade atividade) {
        return new LookupDTO(atividade.getId(), atividade.getNome());
    }
}
