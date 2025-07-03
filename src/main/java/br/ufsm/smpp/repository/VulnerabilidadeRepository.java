package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Vulnerabilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VulnerabilidadeRepository extends JpaRepository<Vulnerabilidade, UUID> {
}
