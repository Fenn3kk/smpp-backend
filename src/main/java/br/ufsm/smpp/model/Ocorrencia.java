package br.ufsm.smpp.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ocorrencia")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Ocorrencia {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ocorrencia_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TipoOcorrencia tipoOcorrencia;

    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 2000)
    private String descricao;

    @OneToMany(mappedBy = "ocorrencia", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FotoOcorrencia> fotos;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriedade_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Propriedade propriedade;

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
