package br.ufsm.smpp.controller;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.service.IncidenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas a Incidentes.
 * Incidentes representam danos ou perdas que podem ser associados a uma ocorrência
 * (ex: "Perda de lavoura", "Dano estrutural").
 * Este controlador expõe endpoints para listar, buscar, criar e deletar incidentes.
 * A criação e exclusão são restritas a usuários com perfil de 'ADMIN'.
 */
@RestController
@RequestMapping("/incidentes")
@RequiredArgsConstructor
public class IncidenteController {

    /**
     * Serviço que contém a lógica de negócio para os incidentes.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final IncidenteService incidenteService;

    /**
     * Endpoint para listar todos os incidentes cadastrados.
     * Retorna uma lista simplificada no formato LookupDTO (id, nome), ideal para
     * popular listas de seleção no front-end.
     *
     * @return ResponseEntity com status 200 (OK) e a lista de DTOs no corpo da resposta.
     */
    @GetMapping
    public ResponseEntity<List<LookupDTO>> listarTodos() {
        return ResponseEntity.ok(incidenteService.listarTodos());
    }

    /**
     * Endpoint para buscar um incidente específico pelo seu ID.
     *
     * @param id O UUID do incidente a ser buscado, fornecido como uma variável de caminho (path variable).
     * @return ResponseEntity com status 200 (OK) e os dados do incidente no formato LookupDTO.
     *         Se o incidente não for encontrado, o serviço lançará uma exceção,
     *         que será tratada por um @ControllerAdvice.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LookupDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(incidenteService.buscarDtoPorId(id));
    }

    /**
     * Endpoint para criar um novo incidente.
     * Esta operação é restrita a usuários com o perfil 'ADMIN'.
     *
     * @param dto O corpo da requisição contendo os dados do novo incidente (apenas o nome é necessário).
     * @return ResponseEntity com status 201 (CREATED) e os dados do incidente salvo no corpo.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LookupDTO> salvar(@RequestBody LookupDTO dto) {
        LookupDTO salvo = incidenteService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * Endpoint para excluir um incidente pelo seu ID.
     * Esta operação é restrita a usuários com o perfil 'ADMIN'.
     *
     * @param id O UUID do incidente a ser excluído.
     * @return ResponseEntity com status 204 (No Content) indicando sucesso na remoção.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        incidenteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}