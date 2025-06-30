package br.ufsm.smpp.service;

import br.ufsm.smpp.model.BuscaDTO;
import br.ufsm.smpp.model.atividade.Atividade;
import br.ufsm.smpp.model.atividade.AtividadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public List<BuscaDTO> listarTodas() {
        return atividadeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Atividade buscarEntidadePorId(UUID id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada com ID: " + id));
    }

    public BuscaDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    private BuscaDTO toDto(Atividade atividade) {
        return new BuscaDTO(atividade.getId(), atividade.getNome());
    }
}
