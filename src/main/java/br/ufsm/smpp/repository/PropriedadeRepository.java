package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, UUID> {

    List<Propriedade> findByUsuarioId(UUID usuarioId);

    void delete(Propriedade propriedade);
}

