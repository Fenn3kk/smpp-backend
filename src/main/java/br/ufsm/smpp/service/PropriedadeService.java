package br.ufsm.smpp.service;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.model.Cidade;
import br.ufsm.smpp.model.Propriedade;
import br.ufsm.smpp.dto.PropriedadeDTOs;
import br.ufsm.smpp.repository.PropriedadeRepository;
import br.ufsm.smpp.model.Usuario;
import br.ufsm.smpp.model.Vulnerabilidade;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import br.ufsm.smpp.model.Atividade;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
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

    public List<PropriedadeDTOs.Response> listarPorUsuario(UUID usuarioId) {
        return propriedadeRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<PropriedadeDTOs.Response> listarTodas() {
        return propriedadeRepository.findAll(Sort.by("nome")).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public PropriedadeDTOs.Response buscarDtoPorId(UUID id) {
        return toResponseDto(buscarEntidadePorId(id));
    }

    @Transactional
    public PropriedadeDTOs.Response criarPropriedade(PropriedadeDTOs.Request dto, Usuario usuarioLogado) {
        Propriedade propriedade = new Propriedade();
        mapearDtoParaEntidade(propriedade, dto, usuarioLogado);
        Propriedade propriedadeSalva = propriedadeRepository.save(propriedade);
        return toResponseDto(propriedadeSalva);
    }

    @Transactional
    public PropriedadeDTOs.Response atualizarPropriedade(UUID id, PropriedadeDTOs.Request dto, Usuario usuarioLogado) {
        Propriedade propriedadeExistente = buscarEntidadePorId(id);
        validarPermissao(propriedadeExistente, usuarioLogado);

        mapearDtoParaEntidade(propriedadeExistente, dto, usuarioLogado);
        Propriedade propriedadeAtualizada = propriedadeRepository.save(propriedadeExistente);
        return toResponseDto(propriedadeAtualizada);
    }

    @Transactional
    public void deletar(UUID id, Usuario usuarioLogado) {
        Propriedade propriedade = buscarEntidadePorId(id);
        validarPermissao(propriedade, usuarioLogado);
        propriedadeRepository.delete(propriedade);
    }

    private void mapearDtoParaEntidade(Propriedade propriedade, PropriedadeDTOs.Request dto, Usuario usuario) {
        // Busca as entidades relacionadas usando os serviços
        Cidade cidade = cidadeService.buscarEntidadePorId(dto.cidadeId());

        List<Atividade> atividades = dto.atividades().stream()
                .map(atividadeService::buscarEntidadePorId)
                .collect(Collectors.toList());

        List<Vulnerabilidade> vulnerabilidades = dto.vulnerabilidades() != null
                ? dto.vulnerabilidades().stream()
                .map(vulnerabilidadeService::buscarPorId)
                .collect(Collectors.toList())
                : List.of();

        propriedade.setNome(dto.nome());
        propriedade.setCidade(cidade);
        propriedade.setCoordenadas(dto.coordenadas());
        propriedade.setAtividades(atividades);
        propriedade.setVulnerabilidades(vulnerabilidades);
        propriedade.setUsuario(usuario);

        if (dto.proprietario() == null || dto.proprietario().isBlank()) {
            propriedade.setProprietario(usuario.getNome());
            propriedade.setTelefoneProprietario(usuario.getTelefone());
        } else {
            propriedade.setProprietario(dto.proprietario());
            propriedade.setTelefoneProprietario(dto.telefoneProprietario());
        }
    }

    public Propriedade buscarEntidadePorId(UUID id) {
        return propriedadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada com o ID: " + id));
    }

    private void validarPermissao(Propriedade propriedade, Usuario usuarioLogado) {
        if (!propriedade.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Usuário não autorizado a modificar esta propriedade.");
        }
    }

    private PropriedadeDTOs.Response toResponseDto(Propriedade p) {
        return new PropriedadeDTOs.Response(
                p.getId(),
                p.getNome(),
                new LookupDTO(p.getCidade().getId(), p.getCidade().getNome()),
                p.getCoordenadas(),
                p.getProprietario(),
                p.getTelefoneProprietario(),
                p.getAtividades().stream().map(a -> new LookupDTO(a.getId(), a.getNome())).collect(Collectors.toList()),
                p.getVulnerabilidades().stream().map(v -> new LookupDTO(v.getId(), v.getNome())).collect(Collectors.toList()),
                new PropriedadeDTOs.SimpleUserDTO(p.getUsuario().getId(), p.getUsuario().getNome())
        );
    }
}
