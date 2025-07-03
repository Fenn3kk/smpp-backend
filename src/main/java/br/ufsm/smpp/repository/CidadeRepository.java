package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CidadeRepository extends JpaRepository<Cidade, UUID> {
}
