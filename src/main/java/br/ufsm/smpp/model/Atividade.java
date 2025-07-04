package br.ufsm.smpp.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa uma atividade econômica que pode ser realizada em uma propriedade rural.
 * Exemplos incluem "Criação de bovinos", "Lavoura Temporária", etc.
 * Esta entidade é usada para criar uma lista de opções padronizadas no sistema.
 */
@Entity
@Table(name = "atividade")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Atividade {

    /**
     * Identificador único da atividade, gerado automaticamente como um UUID.
     * Atua como a chave primária na tabela 'atividade'.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * O nome descritivo da atividade.
     * Este campo não pode ser nulo ou vazio e deve ser único para garantir
     * que não haja tipos de atividade duplicados no sistema.
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}