package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Cidade;
import br.ufsm.smpp.repository.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    public List<LookupDTO> listarTodas() {
        return cidadeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Cidade buscarEntidadePorId(UUID id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com ID: " + id));
    }

    public LookupDTO buscarDtoPorId(UUID id) {
        Cidade cidade = buscarEntidadePorId(id);
        return toDto(cidade);
    }

    private LookupDTO toDto(Cidade cidade) {
        return new LookupDTO(cidade.getId(), cidade.getNome());
    }
}
