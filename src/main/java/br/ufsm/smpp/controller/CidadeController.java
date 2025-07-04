package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.service.CidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas a Cidades.
 * Expõe endpoints para listar e buscar cidades, que são usadas principalmente
 * para popular listas de seleção no front-end ao cadastrar uma propriedade.
 */
@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {

    /**
     * Serviço que contém a lógica de negócio para as cidades.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final CidadeService cidadeService;

    /**
     * Endpoint para listar todas as cidades cadastradas.
     * Retorna uma lista simplificada no formato LookupDTO (id, nome).
     *
     * @return ResponseEntity com status 200 (OK) e a lista de DTOs no corpo da resposta.
     */
    @GetMapping
    public ResponseEntity<List<LookupDTO>> listarTodas() {
        return ResponseEntity.ok(cidadeService.listarTodas());
    }

    /**
     * Endpoint para buscar uma cidade específica pelo seu ID.
     *
     * @param id O UUID da cidade a ser buscada, fornecido como uma variável de caminho (path variable).
     * @return ResponseEntity com status 200 (OK) e os dados da cidade no formato LookupDTO.
     *         Se a cidade não for encontrada, o serviço lançará uma exceção,
     *         que será tratada por um @ControllerAdvice.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LookupDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cidadeService.buscarDtoPorId(id));
    }
}