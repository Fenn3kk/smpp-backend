package br.ufsm.smpp.service;

import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.model.propriedade.PropriedadeRepository;
import br.ufsm.smpp.model.usuario.Usuario;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;

    public List<Propriedade> listarTodas() {
        return propriedadeRepository.findAll();
    }

    public Propriedade buscarPorId(UUID id) {
        return propriedadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada"));
    }

    public Propriedade salvar(Propriedade propriedade) {
        // Se não vier nome e telefone do proprietário, preenche com os dados do usuário logado
        if ((propriedade.getProprietario() == null || propriedade.getProprietario().isBlank())
                && (propriedade.getTelefoneProprietario() == null || propriedade.getTelefoneProprietario().isBlank())) {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Usuario usuario) {
                propriedade.setProprietario(usuario.getNome());
                propriedade.setTelefoneProprietario(usuario.getTelefone());
            }
        }

        return propriedadeRepository.save(propriedade);
    }

    public Propriedade atualizar(UUID id, Propriedade atualizada) {
        Propriedade existente = buscarPorId(id);
        existente.setNome(atualizada.getNome());
        existente.setCidade(atualizada.getCidade());
        existente.setCoordenadas(atualizada.getCoordenadas());
        existente.setProprietario(atualizada.getProprietario());
        existente.setTelefoneProprietario(atualizada.getTelefoneProprietario());
        existente.setUsuario(atualizada.getUsuario());
        existente.setAtividades(atualizada.getAtividades());
        existente.setVulnerabilidades(atualizada.getVulnerabilidades());
        return propriedadeRepository.save(existente);
    }

    public void deletar(UUID id) {
        Propriedade propriedade = buscarPorId(id);
        propriedadeRepository.delete(propriedade);
    }
}
