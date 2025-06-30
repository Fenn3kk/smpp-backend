package br.ufsm.smpp.model.ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, UUID> {
        List<Ocorrencia> findByPropriedadeId(UUID propriedade);
}
