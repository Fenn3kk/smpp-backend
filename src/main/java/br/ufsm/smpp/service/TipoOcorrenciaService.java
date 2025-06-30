package br.ufsm.smpp.service;

import br.ufsm.smpp.model.BuscaDTO;
import br.ufsm.smpp.model.ocorrencia.tipo_ocorrencia.TipoOcorrencia;
import br.ufsm.smpp.model.ocorrencia.tipo_ocorrencia.TipoOcorrenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TipoOcorrenciaService {

    private final TipoOcorrenciaRepository repository;

    public List<BuscaDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public TipoOcorrencia buscarEntidadePorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Ocorrência não encontrado com ID: " + id));
    }

    public BuscaDTO buscarDtoPorId(UUID id) {
        return toDto(buscarEntidadePorId(id));
    }

    private BuscaDTO toDto(TipoOcorrencia tipo) {
        return new BuscaDTO(tipo.getId(), tipo.getNome());
    }
}

