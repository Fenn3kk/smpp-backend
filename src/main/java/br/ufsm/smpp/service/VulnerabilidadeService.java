package br.ufsm.smpp.service;

import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import br.ufsm.smpp.model.vulnerabilidade.VulnerabilidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VulnerabilidadeService {

    private final VulnerabilidadeRepository vulnerabilidadeRepository;

    public List<Vulnerabilidade> listarTodas() {
        return vulnerabilidadeRepository.findAll();
    }

    public Vulnerabilidade buscarPorId(UUID id) {
        return vulnerabilidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vulnerabilidade não encontrada"));
    }
}

