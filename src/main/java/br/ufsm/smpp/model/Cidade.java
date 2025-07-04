package br.ufsm.smpp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Representa uma cidade no sistema.
 * Esta entidade é usada para criar uma lista de opções de cidades
 * que podem ser associadas a outras entidades, como uma Propriedade.
 */
@Entity
@Table(name = "cidade")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Cidade {

    /**
     * Identificador único da cidade, gerado automaticamente como um UUID.
     * Atua como a chave primária na tabela 'cidade'.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * O nome da cidade.
     * Este campo não pode ser nulo ou vazio e deve ser único para garantir
     * que não haja cidades duplicadas no sistema.
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}