package br.ufsm.smpp.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa um tipo de incidente ou dano que pode ocorrer como resultado de uma ocorrência.
 * Exemplos incluem "Perda de animais", "Perda de lavoura" ou "Dano estrutural".
 * Esta entidade é usada para criar uma lista de opções padronizadas no sistema.
 */
@Entity
@Table(name = "incidente")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Incidente {

    /**
     * Identificador único do incidente, gerado automaticamente como um UUID.
     * Atua como a chave primária na tabela 'incidente'.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * O nome descritivo do incidente.
     * Este campo não pode ser nulo ou vazio e deve ser único para garantir
     * que não haja tipos de incidentes duplicados no sistema.
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}