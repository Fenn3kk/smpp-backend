package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.Propriedade;
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

@RestController
@RequestMapping("/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeService propriedadeService;

    @GetMapping
    public ResponseEntity<List<PropriedadeDTOs.Response>> listarPorUsuario(@AuthenticationPrincipal Usuario usuario) {
        List<PropriedadeDTOs.Response> propriedades = propriedadeService.listarPorUsuario(usuario.getId());
        return ResponseEntity.ok(propriedades);
    }
    
    @GetMapping("/todas")
    public ResponseEntity<List<PropriedadeDTOs.Response>> listarTodasParaRelatorio() {
        List<PropriedadeDTOs.Response> propriedades = propriedadeService.listarTodas();
        return ResponseEntity.ok(propriedades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropriedadeDTOs.Response> buscarPorId(@PathVariable UUID id) {
        PropriedadeDTOs.Response propriedade = propriedadeService.buscarDtoPorId(id);
        return ResponseEntity.ok(propriedade);
    }

    @PostMapping
    public ResponseEntity<PropriedadeDTOs.Response> salvar(
            @RequestBody @Valid PropriedadeDTOs.Request dto,
            @AuthenticationPrincipal Usuario usuario) {

        PropriedadeDTOs.Response propriedadeSalva = propriedadeService.criarPropriedade(dto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(propriedadeSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropriedadeDTOs.Response> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid PropriedadeDTOs.Request dto,
            @AuthenticationPrincipal Usuario usuario) {

        PropriedadeDTOs.Response propriedadeAtualizada = propriedadeService.atualizarPropriedade(id, dto, usuario);
        return ResponseEntity.ok(propriedadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuario) {

        propriedadeService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}


