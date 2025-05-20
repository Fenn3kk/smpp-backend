package br.ufsm.smpp.model.atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {
}
