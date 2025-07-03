package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, UUID> {
        @Query("SELECT o FROM Ocorrencia o WHERE o.propriedade.id = :propriedadeId ORDER BY o.tipoOcorrencia.nome ASC")
        List<Ocorrencia> findByPropriedadeIdOrderByTipoOcorrenciaNome(@Param("propriedadeId") UUID propriedadeId);

}
