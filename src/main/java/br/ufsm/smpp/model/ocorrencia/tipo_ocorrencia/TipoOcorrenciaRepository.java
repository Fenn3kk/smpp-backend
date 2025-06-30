package br.ufsm.smpp.model.ocorrencia.tipo_ocorrencia;

import br.ufsm.smpp.model.ocorrencia.tipo_ocorrencia.TipoOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoOcorrenciaRepository extends JpaRepository<TipoOcorrencia, UUID> {
}
