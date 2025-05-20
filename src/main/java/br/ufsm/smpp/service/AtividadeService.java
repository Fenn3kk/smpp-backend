package br.ufsm.smpp.service;

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

    public List<Atividade> listarTodas() {
        return atividadeRepository.findAll();
    }

    public Atividade buscarPorId(UUID id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));
    }

    /* public Atividade salvar(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }

    public Atividade atualizar(UUID id, Atividade atualizada) {
        Atividade existente = buscarPorId(id);
        existente.setNome(atualizada.getNome());
        return atividadeRepository.save(existente);
    }

    public void deletar(UUID id) {
        Atividade atividade = buscarPorId(id);
        atividadeRepository.delete(atividade);
    } */
}
