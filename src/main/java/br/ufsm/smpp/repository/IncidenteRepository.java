package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IncidenteRepository extends JpaRepository<Incidente, UUID> {
}
