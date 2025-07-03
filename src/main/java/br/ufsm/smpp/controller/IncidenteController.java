package br.ufsm.smpp.controller;
import br.ufsm.smpp.dto.LookupDTO;
import br.ufsm.smpp.service.IncidenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/incidentes")
@RequiredArgsConstructor
public class IncidenteController {

    private final IncidenteService incidenteService;

    @GetMapping
    public ResponseEntity<List<LookupDTO>> listarTodos() {
        return ResponseEntity.ok(incidenteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LookupDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(incidenteService.buscarDtoPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LookupDTO> salvar(@RequestBody LookupDTO dto) {
        LookupDTO salvo = incidenteService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        incidenteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
