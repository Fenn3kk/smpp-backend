package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.OcorrenciaDTOs;
import br.ufsm.smpp.service.OcorrenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas a Ocorrências.
 * Expõe endpoints para criar, listar, atualizar e deletar ocorrências,
 * incluindo o tratamento de upload de fotos associadas.
 */
@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
public class OcorrenciaController {

    /**
     * Serviço que contém a lógica de negócio para as ocorrências.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final OcorrenciaService ocorrenciaService;

    /**
     * Endpoint para listar todas as ocorrências associadas a uma propriedade específica.
     *
     * @param propriedadeId O UUID da propriedade cujas ocorrências serão listadas.
     * @return ResponseEntity com status 200 (OK) e a lista de ocorrências no formato DTO.
     */
    @GetMapping("/propriedade/{propriedadeId}")
    public ResponseEntity<List<OcorrenciaDTOs.Response>> listarPorPropriedade(@PathVariable UUID propriedadeId) {
        List<OcorrenciaDTOs.Response> ocorrencias = ocorrenciaService.listarPorPropriedade(propriedadeId);
        return ResponseEntity.ok(ocorrencias);
    }

    /**
     * Endpoint para criar uma nova ocorrência, permitindo o envio de fotos simultaneamente.
     * A requisição deve ser do tipo 'multipart/form-data'.
     *
     * @param dto A parte da requisição contendo os dados da ocorrência em formato JSON.
     * @param fotos Uma lista opcional de arquivos de imagem a serem associados à ocorrência.
     * @return ResponseEntity com status 201 (CREATED) e os dados da ocorrência salva.
     * @throws IOException se ocorrer um erro durante o processamento dos arquivos.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcorrenciaDTOs.Response> salvar(
            @RequestPart("ocorrenciaDto") @Valid OcorrenciaDTOs.Request dto,
            @RequestPart(name = "fotos", required = false) List<MultipartFile> fotos) throws IOException {

        OcorrenciaDTOs.Response ocorrenciaSalva = ocorrenciaService.salvarComFotos(dto, fotos);
        return ResponseEntity.status(HttpStatus.CREATED).body(ocorrenciaSalva);
    }

    /**
     * Endpoint para atualizar uma ocorrência existente. Permite adicionar novas fotos e
     * solicitar a exclusão de fotos existentes através do DTO.
     * A requisição deve ser do tipo 'multipart/form-data'.
     *
     * @param id O UUID da ocorrência a ser atualizada.
     * @param dto A parte da requisição com os dados de atualização da ocorrência.
     * @param novasFotos Uma lista opcional de novos arquivos de imagem a serem adicionados.
     * @return ResponseEntity com status 200 (OK) e os dados da ocorrência atualizada.
     * @throws IOException se ocorrer um erro durante o processamento dos novos arquivos.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcorrenciaDTOs.Response> atualizar(
            @PathVariable UUID id,
            @RequestPart("ocorrenciaUpdateDto") @Valid OcorrenciaDTOs.UpdateRequest dto,
            @RequestPart(name = "novasFotos", required = false) List<MultipartFile> novasFotos) throws IOException {

        OcorrenciaDTOs.Response atualizada = ocorrenciaService.updateOcorrencia(id, dto, novasFotos);
        return ResponseEntity.ok(atualizada);
    }

    /**
     * Endpoint para excluir uma ocorrência pelo seu ID.
     * A exclusão em cascata (cascade) removerá também as fotos associadas.
     *
     * @param id O UUID da ocorrência a ser excluída.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna status 204 No Content em caso de sucesso.
    public void excluirOcorrencia(@PathVariable UUID id) {
        ocorrenciaService.excluir(id);
    }
}