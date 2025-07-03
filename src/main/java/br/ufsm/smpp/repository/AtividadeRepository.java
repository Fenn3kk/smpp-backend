package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {
}
