package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.service.TipoOcorrenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas a Tipos de Ocorrência.
 * Expõe endpoints para listar e buscar tipos de ocorrência, que são usados
 * para popular listas de seleção no front-end (ex: Alagamento, Seca).
 */
@RestController
@RequestMapping("/tipo-ocorrencia")
@RequiredArgsConstructor
public class TipoOcorrenciaController {

    /**
     * Serviço que contém a lógica de negócio para os tipos de ocorrência.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final TipoOcorrenciaService service;

    /**
     * Endpoint para listar todos os tipos de ocorrência cadastrados.
     * Retorna uma lista simplificada no formato LookupDTO (id, nome).
     *
     * @return ResponseEntity com status 200 (OK) e a lista de DTOs no corpo da resposta.
     */
    @GetMapping
    public ResponseEntity<List<LookupDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Endpoint para buscar um tipo de ocorrência específico pelo seu ID.
     *
     * @param id O UUID do tipo de ocorrência a ser buscado, fornecido como uma variável de caminho (path variable).
     * @return ResponseEntity com status 200 (OK) e os dados do tipo de ocorrência no formato LookupDTO.
     *         Se o tipo de ocorrência não for encontrado, o serviço lançará uma exceção,
     *         que será tratada por um @ControllerAdvice.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LookupDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarDtoPorId(id));
    }
}