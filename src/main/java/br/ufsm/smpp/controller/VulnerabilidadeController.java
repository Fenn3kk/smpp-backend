package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.LookupDTO; // Importe o DTO
import br.ufsm.smpp.model.Vulnerabilidade;
import br.ufsm.smpp.service.VulnerabilidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas a Vulnerabilidades.
 * Expõe endpoints para listar e buscar vulnerabilidades, que são usadas
 * para popular listas de seleção no front-end.
 */
@RestController
@RequestMapping("/vulnerabilidades")
@RequiredArgsConstructor
public class VulnerabilidadeController {

    /**
     * Serviço que contém a lógica de negócio para as vulnerabilidades.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final VulnerabilidadeService vulnerabilidadeService;

    /**
     * Endpoint para listar todas as vulnerabilidades cadastradas.
     * Retorna uma lista de vulnerabilidades.
     *
     * @return ResponseEntity com status 200 (OK) e a lista de vulnerabilidades no corpo.
     */
    @GetMapping
    public ResponseEntity<List<LookupDTO>> listarTodas() {
        return ResponseEntity.ok(vulnerabilidadeService.listarTodas());
    }

    /**
     * Endpoint para buscar uma vulnerabilidade específica pelo seu ID.
     *
     * @param id O UUID da vulnerabilidade a ser buscada, fornecido como uma variável de caminho (path variable).
     * @return ResponseEntity com status 200 (OK) e os dados da vulnerabilidade.
     *         Se a vulnerabilidade não for encontrada, o serviço pode lançar uma exceção,
     *         que seria tratada por um @ControllerAdvice.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vulnerabilidade> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(vulnerabilidadeService.buscarPorId(id));
    }
}