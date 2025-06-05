package br.ufsm.smpp.model.cidade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CidadeRepository extends JpaRepository<Cidade, UUID> {
}
