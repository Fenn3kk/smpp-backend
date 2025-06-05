package br.ufsm.smpp.service;

import br.ufsm.smpp.model.cidade.Cidade;
import br.ufsm.smpp.model.cidade.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    public List<Cidade> listarTodas() {
        return cidadeRepository.findAll();
    }

    public Cidade buscarPorId(UUID id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada: " + id));
    }

    // Se quiser, pode adicionar método para salvar, atualizar, deletar, etc.
}
