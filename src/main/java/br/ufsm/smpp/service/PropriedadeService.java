package br.ufsm.smpp.service;

import br.ufsm.smpp.model.propriedade.cidade.Cidade;
import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.model.propriedade.PropriedadeDTO;
import br.ufsm.smpp.model.propriedade.PropriedadeRepository;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import br.ufsm.smpp.model.atividade.Atividade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;
    private final AtividadeService atividadeService;
    private final VulnerabilidadeService vulnerabilidadeService;
    private final CidadeService cidadeService;

    public List<Propriedade> listarPorUsuario(UUID usuarioId) {
        return propriedadeRepository.findByUsuarioId(usuarioId);
    }

    public Propriedade buscarPorId(UUID id) {
        return propriedadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada com o ID: " + id));
    }

    @Transactional
    public Propriedade criarPropriedade(PropriedadeDTO dto, Usuario usuarioLogado) {
        Propriedade propriedade = new Propriedade();
        // Mapeia os dados do DTO para a nova entidade
        DtoParaEntidade(propriedade, dto, usuarioLogado);
        return propriedadeRepository.save(propriedade);
    }

    @Transactional
    public Propriedade atualizarPropriedade(UUID id, PropriedadeDTO dto, Usuario usuarioLogado) {
        // 1. Busca a entidade existente no banco
        Propriedade propriedadeExistente = buscarPorId(id);

        // 2. [Opcional mas recomendado] Verifica se o usuário logado pode editar esta propriedade
        if (!propriedadeExistente.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Usuário não autorizado a editar esta propriedade.");
        }

        // 3. Mapeia os novos dados do DTO para a entidade existente
        DtoParaEntidade(propriedadeExistente, dto, usuarioLogado);

        // 4. Salva a entidade atualizada
        return propriedadeRepository.save(propriedadeExistente);
    }

    @Transactional
    public void deletar(UUID id, Usuario usuarioLogado) {
        Propriedade propriedade = buscarPorId(id);

        // Verifica a permissão antes de deletar
        if (!propriedade.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Usuário não autorizado a deletar esta propriedade.");
        }

        propriedadeRepository.delete(propriedade);
    }

    /**
     * Método auxiliar centralizado para mapear dados de um DTO para uma entidade Propriedade.
     * Isso evita duplicação de código entre os métodos de criar e atualizar.
     */
    private void DtoParaEntidade(Propriedade propriedade, PropriedadeDTO dto, Usuario usuario) {
        Cidade cidade = cidadeService.buscarPorId(dto.getCidadeId());

        List<Atividade> atividades = dto.getAtividades().stream()
                .map(atividadeService::buscarPorId)
                .collect(Collectors.toList());

        List<Vulnerabilidade> vulnerabilidades = dto.getVulnerabilidades().stream()
                .map(vulnerabilidadeService::buscarPorId)
                .collect(Collectors.toList());

        propriedade.setNome(dto.getNome());
        propriedade.setCidade(cidade);
        propriedade.setCoordenadas(dto.getCoordenadas());
        propriedade.setAtividades(atividades);
        propriedade.setVulnerabilidades(vulnerabilidades);

        // Define o usuário que está realizando a operação
        propriedade.setUsuario(usuario);

        // Lógica para definir o proprietário: se o DTO não informar, usa o usuário logado.
        if (dto.getProprietario() == null || dto.getProprietario().isBlank()) {
            propriedade.setProprietario(usuario.getNome());
            propriedade.setTelefoneProprietario(usuario.getTelefone());
        } else {
            propriedade.setProprietario(dto.getProprietario());
            propriedade.setTelefoneProprietario(dto.getTelefoneProprietario());
        }
    }
}
