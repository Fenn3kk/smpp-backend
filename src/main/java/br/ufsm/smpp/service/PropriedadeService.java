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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócio relacionada à entidade Propriedade.
 * Gerencia a criação, leitura, atualização e exclusão (CRUD) de propriedades,
 * além de realizar a conversão entre entidades e DTOs.
 */
@Service
@RequiredArgsConstructor
public class PropriedadeService {

    // Repositórios e serviços necessários para operações com propriedades.
    private final PropriedadeRepository propriedadeRepository;
    private final AtividadeService atividadeService;
    private final VulnerabilidadeService vulnerabilidadeService;
    private final CidadeService cidadeService;

    /**
     * Lista todas as propriedades pertencentes a um usuário específico.
     * @param usuarioId O ID do usuário cujas propriedades serão listadas.
     * @return Uma lista de {@link PropriedadeDTOs.Response} representando as propriedades.
     */
    public List<PropriedadeDTOs.Response> listarPorUsuario(UUID usuarioId) {
        return propriedadeRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as propriedades cadastradas no sistema, ordenadas por nome.
     * @return Uma lista de {@link PropriedadeDTOs.Response} com todas as propriedades.
     */
    public List<PropriedadeDTOs.Response> listarTodas() {
        return propriedadeRepository.findAll(Sort.by("nome")).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca uma propriedade pelo seu ID e a retorna como um DTO de resposta.
     * @param id O UUID da propriedade a ser buscada.
     * @return O {@link PropriedadeDTOs.Response} correspondente.
     * @throws EntityNotFoundException se a propriedade não for encontrada.
     */
    public PropriedadeDTOs.Response buscarDtoPorId(UUID id) {
        return toResponseDto(buscarEntidadePorId(id));
    }

    /**
     * Cria uma nova propriedade no banco de dados.
     * A operação é transacional, garantindo a consistência dos dados.
     * @param dto O DTO com os dados da nova propriedade.
     * @param usuarioLogado O usuário autenticado que está criando a propriedade.
     * @return O {@link PropriedadeDTOs.Response} da propriedade recém-criada.
     */
    @Transactional
    public PropriedadeDTOs.Response criarPropriedade(PropriedadeDTOs.Request dto, Usuario usuarioLogado) {
        Propriedade propriedade = new Propriedade();
        mapearDtoParaEntidade(propriedade, dto, usuarioLogado);
        Propriedade propriedadeSalva = propriedadeRepository.save(propriedade);
        return toResponseDto(propriedadeSalva);
    }

    /**
     * Atualiza uma propriedade existente.
     * Primeiro, verifica se o usuário logado tem permissão para modificar a propriedade.
     * A operação é transacional.
     * @param id O UUID da propriedade a ser atualizada.
     * @param dto O DTO com os novos dados da propriedade.
     * @param usuarioLogado O usuário autenticado que está realizando a atualização.
     * @return O {@link PropriedadeDTOs.Response} da propriedade atualizada.
     * @throws EntityNotFoundException se a propriedade não for encontrada.
     * @throws AccessDeniedException se o usuário não for o proprietário do recurso.
     */
    @Transactional
    public PropriedadeDTOs.Response atualizarPropriedade(UUID id, PropriedadeDTOs.Request dto, Usuario usuarioLogado) {
        Propriedade propriedadeExistente = buscarEntidadePorId(id);
        validarPermissao(propriedadeExistente, usuarioLogado);

        mapearDtoParaEntidade(propriedadeExistente, dto, usuarioLogado);
        Propriedade propriedadeAtualizada = propriedadeRepository.save(propriedadeExistente);
        return toResponseDto(propriedadeAtualizada);
    }

    /**
     * Deleta uma propriedade do banco de dados.
     * Primeiro, verifica se o usuário logado tem permissão para deletar a propriedade.
     * A operação é transacional.
     * @param id O UUID da propriedade a ser deletada.
     * @param usuarioLogado O usuário autenticado que está realizando a exclusão.
     * @throws EntityNotFoundException se a propriedade não for encontrada.
     * @throws AccessDeniedException se o usuário não for o proprietário do recurso.
     */
    @Transactional
    public void deletar(UUID id, Usuario usuarioLogado) {
        Propriedade propriedade = buscarEntidadePorId(id);
        validarPermissao(propriedade, usuarioLogado);
        propriedadeRepository.delete(propriedade);
    }

    /**
     * Mapeia os dados de um DTO de requisição para uma entidade Propriedade.
     * Este método busca as entidades relacionadas (Cidade, Atividades, etc.) e as associa.
     * @param propriedade A entidade Propriedade a ser preenchida ou atualizada.
     * @param dto O DTO de origem dos dados.
     * @param usuario O usuário a ser associado como dono da propriedade.
     */
    private void mapearDtoParaEntidade(Propriedade propriedade, PropriedadeDTOs.Request dto, Usuario usuario) {
        // Busca as entidades relacionadas usando os serviços
        Cidade cidade = cidadeService.buscarEntidadePorId(dto.cidadeId());

        List<Atividade> atividades = dto.atividades().stream()
                .map(atividadeService::buscarEntidadePorId)
                .collect(Collectors.toList());

        List<Vulnerabilidade> vulnerabilidades = Optional.ofNullable(dto.vulnerabilidades())
                .orElse(Collections.emptyList())
                .stream()
                .map(vulnerabilidadeService::buscarPorId)
                .collect(Collectors.toList());

        propriedade.setNome(dto.nome());
        propriedade.setCidade(cidade);
        propriedade.setCoordenadas(dto.coordenadas());
        propriedade.setAtividades(atividades);
        propriedade.setVulnerabilidades(vulnerabilidades);
        propriedade.setUsuario(usuario);

        // Se o proprietário não for informado no DTO, assume os dados do usuário logado.
        if (dto.proprietario() == null || dto.proprietario().isBlank()) {
            propriedade.setProprietario(usuario.getNome());
            propriedade.setTelefoneProprietario(usuario.getTelefone());
        } else {
            propriedade.setProprietario(dto.proprietario());
            propriedade.setTelefoneProprietario(dto.telefoneProprietario());
        }
    }

    /**
     * Busca uma entidade Propriedade pelo ID.
     * @param id O UUID da propriedade.
     * @return A entidade {@link Propriedade} encontrada.
     * @throws EntityNotFoundException se nenhuma propriedade for encontrada com o ID fornecido.
     */
    public Propriedade buscarEntidadePorId(UUID id) {
        return propriedadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada com o ID: " + id));
    }

    /**
     * Valida se o usuário logado é o proprietário da propriedade.
     * @param propriedade A propriedade a ser verificada.
     * @param usuarioLogado O usuário que está tentando realizar a operação.
     * @throws AccessDeniedException se os IDs do usuário não coincidirem.
     */
    private void validarPermissao(Propriedade propriedade, Usuario usuarioLogado) {
        if (!propriedade.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Usuário não autorizado a modificar esta propriedade.");
        }
    }

    /**
     * Converte uma entidade {@link Propriedade} para seu DTO de resposta {@link PropriedadeDTOs.Response}.
     * @param p A entidade Propriedade a ser convertida.
     * @return O DTO de resposta correspondente.
     */
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