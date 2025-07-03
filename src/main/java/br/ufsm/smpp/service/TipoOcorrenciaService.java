package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.TipoOcorrencia;
import br.ufsm.smpp.repository.TipoOcorrenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TipoOcorrenciaService {

    private final TipoOcorrenciaRepository repository;

    public List<LookupDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public TipoOcorrencia buscarEntidadePorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Ocorrência não encontrado com ID: " + id));
    }

    public LookupDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    private LookupDTO toDto(TipoOcorrencia tipo) {
        return new LookupDTO(tipo.getId(), tipo.getNome());
    }
}

