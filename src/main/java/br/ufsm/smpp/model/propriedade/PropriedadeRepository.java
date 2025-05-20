package br.ufsm.smpp.model.propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, UUID> {
}

