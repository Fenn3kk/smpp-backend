package br.ufsm.smpp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Representa o registro de uma ocorrência (um evento adverso) em uma propriedade.
 * Esta entidade armazena detalhes sobre o tipo de evento, data, descrição,
 * fotos, a propriedade afetada e os incidentes (danos) resultantes.
 */
@Entity
@Table(name = "ocorrencia")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Ocorrencia {

    /**
     * Identificador único da ocorrência, gerado automaticamente como um UUID.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * O tipo da ocorrência (ex: Alagamento, Seca, Tempestade).
     * É uma relação muitos-para-um, pois um tipo pode estar associado a várias ocorrências.
     * O carregamento é LAZY para otimizar o desempenho.
     * Excluído dos métodos toString() e equals/hashCode para evitar recursão.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ocorrencia_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TipoOcorrencia tipoOcorrencia;

    /**
     * A data em que a ocorrência aconteceu.
     * Não pode ser nula.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    /**
     * Descrição textual detalhada da ocorrência.
     * Limitado a 500 caracteres.
     */
    @Column(length = 500)
    private String descricao;

    /**
     * Lista de fotos associadas a esta ocorrência.
     * É uma relação um-para-muitos (uma ocorrência pode ter várias fotos).
     * {@code cascade = CascadeType.ALL}: Operações (salvar, atualizar, remover) na Ocorrencia
     * serão propagadas para as Fotos associadas.
     * {@code orphanRemoval = true}: Se uma FotoOcorrencia for removida desta lista,
     * ela será automaticamente excluída do banco de dados.
     */
    @OneToMany(mappedBy = "ocorrencia", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FotoOcorrencia> fotos;

    /**
     * A propriedade onde a ocorrência foi registrada.
     * É uma relação muitos-para-um, pois uma propriedade pode ter várias ocorrências.
     * O carregamento é LAZY.
     * Excluído dos métodos toString() e equals/hashCode para evitar recursão.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriedade_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Propriedade propriedade;

    /**
     * Lista de incidentes (danos específicos) resultantes da ocorrência.
     * É uma relação muitos-para-muitos, gerenciada pela tabela 'ocorrencia_incidente'.
     * O carregamento é LAZY.
     * Excluído dos métodos toString() e equals/hashCode.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ocorrencia_incidente",
            joinColumns = @JoinColumn(name = "ocorrencia_id"),
            inverseJoinColumns = @JoinColumn(name = "incidente_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Incidente> incidentes;
}