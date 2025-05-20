package br.ufsm.smpp.model.vulnerabilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VulnerabilidadeRepository extends JpaRepository<Vulnerabilidade, UUID> {
}
