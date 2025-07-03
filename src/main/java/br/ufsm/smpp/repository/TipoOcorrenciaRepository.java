package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.TipoOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TipoOcorrenciaRepository extends JpaRepository<TipoOcorrencia, UUID> {
}
