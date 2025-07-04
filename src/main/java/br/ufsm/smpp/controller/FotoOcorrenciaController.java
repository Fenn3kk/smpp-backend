package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.FotoOcorrenciaDTO;
import br.ufsm.smpp.service.FotoOcorrenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as operações relacionadas aos metadados das fotos de ocorrências.
 * Expõe endpoints para listar as fotos de uma ocorrência e para deletar uma foto específica.
 * O ato de servir o arquivo de imagem em si é geralmente tratado por outro mecanismo
 * (como um endpoint de download ou um servidor de arquivos estáticos).
 */
@RestController
@RequestMapping("/fotos")
@RequiredArgsConstructor
public class FotoOcorrenciaController {

    /**
     * Serviço que contém a lógica de negócio para as fotos das ocorrências.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final FotoOcorrenciaService fotoOcorrenciaService;

    /**
     * Endpoint para listar todas as fotos associadas a uma ocorrência específica.
     *
     * @param ocorrenciaId O UUID da ocorrência cujas fotos devem ser listadas.
     * @return ResponseEntity com status 200 (OK) e uma lista de {@link FotoOcorrenciaDTO} no corpo,
     *         contendo os metadados e a URL de acesso para cada foto.
     */
    @GetMapping("/ocorrencia/{ocorrenciaId}")
    public ResponseEntity<List<FotoOcorrenciaDTO>> listarFotosPorOcorrencia(@PathVariable UUID ocorrenciaId) {
        return ResponseEntity.ok(fotoOcorrenciaService.listarPorOcorrencia(ocorrenciaId));
    }

    /**
     * Endpoint para deletar uma foto específica pelo seu ID.
     * A operação deleta tanto o registro no banco de dados quanto o arquivo físico associado.
     * Requer que o usuário esteja autenticado para realizar a ação.
     *
     * @param fotoId O UUID da foto a ser deletada.
     * @return ResponseEntity com status 204 (No Content) para indicar que a exclusão foi bem-sucedida.
     */
    @DeleteMapping("/{fotoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletarFoto(@PathVariable UUID fotoId) {
        fotoOcorrenciaService.deleteFoto(fotoId);
        return ResponseEntity.noContent().build();
    }
}