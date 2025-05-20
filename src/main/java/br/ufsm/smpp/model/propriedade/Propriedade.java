package br.ufsm.smpp.model.propriedade;
import br.ufsm.smpp.model.atividade.Atividade;
import br.ufsm.smpp.model.usuario.Usuario;
import br.ufsm.smpp.model.vulnerabilidade.Vulnerabilidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Propriedade")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Propriedade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String nome;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String cidade;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String coordenadas;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String proprietario;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String telefoneProprietario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "propriedade_atividade",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "atividade_id")
    )
    private List<Atividade> atividades;

    @ManyToMany
    @JoinTable(
            name = "propriedade_vulnerabilidade",
            joinColumns = @JoinColumn(name = "propriedade_id"),
            inverseJoinColumns = @JoinColumn(name = "vulnerabilidade_id")
    )
    private List<Vulnerabilidade> vulnerabilidades;

    /*@OneToMany(mappedBy = "propriedade")
    private List<Ocorrencia> ocorrencias;*/
}
