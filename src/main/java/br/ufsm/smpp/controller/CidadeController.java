package br.ufsm.smpp.controller;
import br.ufsm.smpp.dto.LookupDTO;
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
    public ResponseEntity<List<LookupDTO>> listarTodas() {
        return ResponseEntity.ok(cidadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LookupDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cidadeService.buscarDtoPorId(id));
    }
}

