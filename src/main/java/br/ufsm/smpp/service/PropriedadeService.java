package br.ufsm.smpp.service;

import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.model.propriedade.PropriedadeDTO;
import br.ufsm.smpp.model.propriedade.PropriedadeRepository;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import br.ufsm.smpp.model.atividade.Atividade;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;
    private final AtividadeService atividadeService; // para buscar por UUID
    private final VulnerabilidadeService vulnerabilidadeService;

    public List<Propriedade> listarPorUsuario(UUID usuarioId) {
        return propriedadeRepository.findByUsuarioId(usuarioId);
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

    public Propriedade fromDTO(PropriedadeDTO dto) {
        Propriedade propriedade = new Propriedade();
        propriedade.setNome(dto.nome);
        propriedade.setCidade(dto.cidade);
        propriedade.setCoordenadas(dto.coordenadas);
        propriedade.setProprietario(dto.proprietario);
        propriedade.setTelefoneProprietario(dto.telefoneProprietario);

        List<Atividade> atividades = dto.atividades.stream()
                .map(atividadeService::buscarPorId)
                .collect(Collectors.toList());
        propriedade.setAtividades(atividades);

        List<Vulnerabilidade> vulnerabilidades = dto.vulnerabilidades.stream()
                .map(vulnerabilidadeService::buscarPorId)
                .collect(Collectors.toList());
        propriedade.setVulnerabilidades(vulnerabilidades);

        return propriedade;
    }

    public Propriedade atualizar(UUID id, Propriedade nova) {
        return propriedadeRepository.findById(id).map(p -> {
            p.setNome(nova.getNome());
            p.setCidade(nova.getCidade());
            p.setCoordenadas(nova.getCoordenadas());

            String novoProprietario = nova.getProprietario();
            String novoTelefone = nova.getTelefoneProprietario();

            if ((novoProprietario == null || novoProprietario.isBlank()) &&
                    (novoTelefone == null || novoTelefone.isBlank())) {

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getPrincipal() instanceof Usuario usuario) {
                    p.setProprietario(usuario.getNome());
                    p.setTelefoneProprietario(usuario.getTelefone());
                } else {
                    p.setProprietario(null);
                    p.setTelefoneProprietario(null);
                }

            } else {
                p.setProprietario(novoProprietario);
                p.setTelefoneProprietario(novoTelefone);
            }

            p.setAtividades(nova.getAtividades());
            p.setVulnerabilidades(nova.getVulnerabilidades());

            return propriedadeRepository.save(p);
        }).orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada com id: " + id));
    }

    public void deletar(UUID id) {
        Propriedade propriedade = buscarPorId(id);
        propriedadeRepository.delete(propriedade);
    }
}
