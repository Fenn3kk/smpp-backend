package br.ufsm.smpp.repository;
import br.ufsm.smpp.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositório para a entidade {@link Cidade}.
 * <p>
 * Estende {@link JpaRepository} para fornecer operações CRUD (Create, Read, Update, Delete)
 * padrão para a entidade Cidade. A anotação @Repository marca esta interface
 * como um componente de persistência gerenciado pelo Spring.
 */
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, UUID> {
}