package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.FotoOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FotoOcorrenciaRepository extends JpaRepository<FotoOcorrencia, UUID> {

    List<FotoOcorrencia> findByOcorrenciaId(UUID ocorrenciaId);
}
