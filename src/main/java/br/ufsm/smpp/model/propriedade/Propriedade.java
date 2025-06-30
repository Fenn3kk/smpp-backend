package br.ufsm.smpp.model.propriedade;
import br.ufsm.smpp.model.atividade.Atividade;
import br.ufsm.smpp.model.propriedade.cidade.Cidade;
import br.ufsm.smpp.model.ocorrencia.Ocorrencia;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "propriedade")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Propriedade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cidade cidade;

    @NotBlank
    @Column(nullable = false)
    private String coordenadas;

    @NotBlank
    @Column(nullable = false)
    private String proprietario;

    @NotBlank
    @Column(name = "telefone_proprietario", nullable = false)
    private String telefoneProprietario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "propriedade_atividade",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "atividade_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Atividade> atividades;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "propriedade_vulnerabilidade",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "vulnerabilidade_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vulnerabilidade> vulnerabilidades;

    @OneToMany(mappedBy = "propriedade")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ocorrencia> ocorrencias;
}