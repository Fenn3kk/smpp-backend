package br.ufsm.smpp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

/**
 * Representa uma propriedade rural no sistema.
 * Esta entidade armazena informações detalhadas sobre uma propriedade,
 * incluindo sua localização, proprietário, atividades econômicas,
 * vulnerabilidades e o usuário que a cadastrou.
 */
@Entity
@Table(name = "propriedade")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Propriedade {

    /**
     * Identificador único da propriedade, gerado automaticamente como um UUID.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Nome da propriedade (ex: "Fazenda Santa Clara").
     * Não pode ser nulo ou vazio.
     */
    @NotBlank
    @Column(nullable = false)
    private String nome;

    /**
     * A cidade onde a propriedade está localizada.
     * É uma relação muitos-para-um (várias propriedades podem pertencer a uma cidade).
     * O carregamento é LAZY para otimizar o desempenho.
     * Excluído dos métodos toString() e equals/hashCode para evitar recursão infinita.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cidade cidade;

    /**
     * Coordenadas geográficas da propriedade (ex: latitude e longitude).
     * Não pode ser nulo ou vazio.
     */
    @NotBlank
    @Column(nullable = false)
    private String coordenadas;

    /**
     * Nome completo do proprietário da terra.
     * Não pode ser nulo ou vazio.
     */
    @NotBlank
    @Column(nullable = false)
    private String proprietario;

    /**
     * Número de telefone para contato com o proprietário.
     * Não pode ser nulo ou vazio.
     */
    @NotBlank
    @Column(name = "telefone_proprietario", nullable = false)
    private String telefoneProprietario;

    /**
     * O usuário do sistema que cadastrou esta propriedade.
     * É uma relação muitos-para-um (um usuário pode cadastrar várias propriedades).
     * O carregamento é LAZY.
     * Excluído dos métodos toString() e equals/hashCode para evitar recursão infinita.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    /**
     * Lista de atividades econômicas realizadas na propriedade.
     * É uma relação muitos-para-muitos, gerenciada por uma tabela de junção 'propriedade_atividade'.
     * O carregamento é LAZY.
     * Excluído dos métodos toString() e equals/hashCode para evitar problemas de performance e recursão.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "propriedade_atividade",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "atividade_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Atividade> atividades;

    /**
     * Lista de vulnerabilidades ambientais ou estruturais associadas à propriedade.
     * É uma relação muitos-para-muitos, gerenciada por uma tabela de junção 'propriedade_vulnerabilidade'.
     * O carregamento é LAZY.
     * Excluído dos métodos toString() e equals/hashCode.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "propriedade_vulnerabilidade",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "vulnerabilidade_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vulnerabilidade> vulnerabilidades;

    /**
     * Lista de ocorrências (incidentes) registradas para esta propriedade.
     * É uma relação um-para-muitos (uma propriedade pode ter várias ocorrências).
     * O campo 'propriedade' na entidade Ocorrencia mapeia este relacionamento.
     * Excluído dos métodos toString() e equals/hashCode.
     */
    @OneToMany(mappedBy = "propriedade")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ocorrencia> ocorrencias;
}