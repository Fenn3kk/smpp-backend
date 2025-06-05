package br.ufsm.smpp.controller;

import br.ufsm.smpp.model.cidade.Cidade;
import br.ufsm.smpp.service.CidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeService cidadeService;

    @GetMapping
    public ResponseEntity<List<Cidade>> listarTodas() {
        List<Cidade> cidades = cidadeService.listarTodas();
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidade> buscarPorId(@PathVariable UUID id) {
        Cidade cidade = cidadeService.buscarPorId(id);
        return ResponseEntity.ok(cidade);
    }
}

