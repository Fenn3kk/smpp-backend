package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.PropriedadeDTOs;
import br.ufsm.smpp.model.Usuario;
import br.ufsm.smpp.service.PropriedadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações CRUD (Criar, Ler, Atualizar, Deletar)
 * relacionadas a Propriedades.
 */
@RestController
@RequestMapping("/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    /**
     * Serviço que contém a lógica de negócio para as propriedades.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final PropriedadeService propriedadeService;

    /**
     * Endpoint para listar todas as propriedades pertencentes ao usuário autenticado.
     *
     * @param usuario O objeto do usuário autenticado, injetado automaticamente pelo Spring Security.
     * @return ResponseEntity com status 200 (OK) e a lista de propriedades do usuário no corpo.
     */
    @GetMapping
    public ResponseEntity<List<PropriedadeDTOs.Response>> listarPorUsuario(@AuthenticationPrincipal Usuario usuario) {
        List<PropriedadeDTOs.Response> propriedades = propriedadeService.listarPorUsuario(usuario.getId());
        return ResponseEntity.ok(propriedades);
    }

    /**
     * Endpoint para listar TODAS as propriedades cadastradas no sistema.
     * Geralmente utilizado para fins de relatórios ou por usuários com permissão de administrador.
     *
     * @return ResponseEntity com status 200 (OK) e a lista de todas as propriedades.
     */
    @GetMapping("/todas")
    public ResponseEntity<List<PropriedadeDTOs.Response>> listarTodasParaRelatorio() {
        List<PropriedadeDTOs.Response> propriedades = propriedadeService.listarTodas();
        return ResponseEntity.ok(propriedades);
    }

    /**
     * Endpoint para buscar uma propriedade específica pelo seu ID.
     *
     * @param id O UUID da propriedade a ser buscada, fornecido como uma variável de caminho (path variable).
     * @return ResponseEntity com status 200 (OK) e os dados da propriedade encontrada.
     *         Se não encontrada, o serviço lançará uma exceção tratada pelo ControllerAdvice.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropriedadeDTOs.Response> buscarPorId(@PathVariable UUID id) {
        PropriedadeDTOs.Response propriedade = propriedadeService.buscarDtoPorId(id);
        return ResponseEntity.ok(propriedade);
    }

    /**
     * Endpoint para criar uma nova propriedade.
     * A propriedade criada será associada ao usuário autenticado.
     *
     * @param dto O corpo da requisição contendo os dados da nova propriedade, validado com @Valid.
     * @param usuario O usuário autenticado que está realizando a operação.
     * @return ResponseEntity com status 201 (CREATED) e os dados da propriedade salva no corpo.
     */
    @PostMapping
    public ResponseEntity<PropriedadeDTOs.Response> salvar(
            @RequestBody @Valid PropriedadeDTOs.Request dto,
            @AuthenticationPrincipal Usuario usuario) {

        PropriedadeDTOs.Response propriedadeSalva = propriedadeService.criarPropriedade(dto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(propriedadeSalva);
    }

    /**
     * Endpoint para atualizar uma propriedade existente.
     * O serviço deve verificar se o usuário autenticado tem permissão para alterar esta propriedade.
     *
     * @param id O UUID da propriedade a ser atualizada.
     * @param dto O corpo da requisição com os novos dados da propriedade.
     * @param usuario O usuário autenticado que está realizando a operação.
     * @return ResponseEntity com status 200 (OK) e os dados da propriedade atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PropriedadeDTOs.Response> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid PropriedadeDTOs.Request dto,
            @AuthenticationPrincipal Usuario usuario) {

        PropriedadeDTOs.Response propriedadeAtualizada = propriedadeService.atualizarPropriedade(id, dto, usuario);
        return ResponseEntity.ok(propriedadeAtualizada);
    }

    /**
     * Endpoint para deletar uma propriedade.
     * O serviço deve verificar se o usuário autenticado tem permissão para deletar esta propriedade.
     *
     * @param id O UUID da propriedade a ser deletada.
     * @param usuario O usuário autenticado que está realizando a operação.
     * @return ResponseEntity com status 204 (No Content) indicando sucesso na remoção.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuario) {

        propriedadeService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}