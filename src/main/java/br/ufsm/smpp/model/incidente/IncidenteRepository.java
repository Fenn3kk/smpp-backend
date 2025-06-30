package br.ufsm.smpp.model.incidente;
import br.ufsm.smpp.model.incidente.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncidenteRepository extends JpaRepository<Incidente, UUID> {
}
