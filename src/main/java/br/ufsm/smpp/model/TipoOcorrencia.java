package br.ufsm.smpp.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

/**
 * Representa um tipo de ocorrência que pode ser registrada no sistema.
 * Exemplos incluem "Alagamento", "Seca", "Tempestade", etc.
 * Esta entidade é usada para categorizar e padronizar os registros de ocorrências.
 */
@Entity
@Table(name = "tipo_ocorrencia")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TipoOcorrencia {

    /**
     * Identificador único do tipo de ocorrência, gerado automaticamente como um UUID.
     * Atua como a chave primária na tabela 'tipo_ocorrencia'.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * O nome descritivo do tipo de ocorrência (ex: "Alagamento").
     * Este campo não pode ser nulo ou vazio e deve ser único para garantir
     * que não haja tipos de ocorrência duplicados no sistema.
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}