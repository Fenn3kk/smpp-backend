package br.ufsm.smpp.model.propriedade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, UUID> {
    List<Propriedade> findByUsuarioId(UUID usuarioId);

    void delete(Propriedade propriedade);
}

