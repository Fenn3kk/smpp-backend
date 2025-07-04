package br.ufsm.smpp.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa um tipo de vulnerabilidade que pode ser associada a uma propriedade.
 * Exemplos incluem "Área sujeita a deslizamentos" ou "Acesso por pontes sujeitas a inundações".
 * Esta entidade é usada para criar uma lista de opções padronizadas no sistema.
 */
@Entity
@Table(name = "vulnerabilidade")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Vulnerabilidade {

    /**
     * Identificador único da vulnerabilidade, gerado automaticamente como um UUID.
     * Atua como a chave primária na tabela 'vulnerabilidade'.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * O nome descritivo da vulnerabilidade.
     * Este campo não pode ser nulo ou vazio e deve ser único para cada registro,
     * garantindo que não haja tipos de vulnerabilidade duplicados.
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}