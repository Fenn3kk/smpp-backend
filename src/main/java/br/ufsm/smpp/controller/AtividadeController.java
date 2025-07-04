package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas a Atividades.
 * Expõe endpoints para listar e buscar atividades.
 */
@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    /**
     * Serviço que contém a lógica de negócio para as atividades.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final AtividadeService atividadeService;

    /**
     * Endpoint para listar todas as atividades cadastradas.
     * Retorna uma lista simplificada de atividades no formato LookupDTO.
     *
     * @return ResponseEntity com status 200 (OK) e a lista de atividades no corpo da resposta.
     */
    @GetMapping
    public ResponseEntity<List<LookupDTO>> listarTodas() {
        return ResponseEntity.ok(atividadeService.listarTodas());
    }

    /**
     * Endpoint para buscar uma atividade específica pelo seu ID.
     *
     * @param id O UUID da atividade a ser buscada, fornecido como uma variável de caminho (path variable).
     * @return ResponseEntity com status 200 (OK) e os dados da atividade no formato LookupDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LookupDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(atividadeService.buscarDtoPorId(id));
    }
}