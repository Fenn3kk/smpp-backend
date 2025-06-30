package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.ocorrencia.Ocorrencia;
import br.ufsm.smpp.model.ocorrencia.OcorrenciaDTO;
import br.ufsm.smpp.model.propriedade.Propriedade;
import br.ufsm.smpp.model.propriedade.PropriedadeDTO;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.service.OcorrenciaService;
import br.ufsm.smpp.service.PropriedadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;
    private final PropriedadeService propriedadeService;

    @GetMapping("/propriedade/{id}")
    public List<Ocorrencia> listarPorPropriedade(@PathVariable UUID id) {
        return ocorrenciaService.listarPorPropriedade(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrencia> buscarPorId(@PathVariable UUID id) {
        return ocorrenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ocorrencia> salvar(
            // CORREÇÃO: Usa @RequestPart para o JSON e para os arquivos
            @RequestPart("ocorrenciaDto") OcorrenciaDTO dto,
            @RequestPart(name = "fotos", required = false) List<MultipartFile> fotos
    ) throws IOException {
        Ocorrencia ocorrenciaSalva = ocorrenciaService.salvar(dto, fotos);

        // MELHORIA: Retorna 201 Created, que é o status correto para criação.
        return ResponseEntity.status(HttpStatus.CREATED).body(ocorrenciaSalva);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        ocorrenciaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
