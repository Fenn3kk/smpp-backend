package br.ufsm.smpp.model.usuario;

import br.ufsm.smpp.model.propriedade.Propriedade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Usuario {

    public enum TipoUsuario {
        ADMIN, COMUM
    }

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
    private String email;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String telefone;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Propriedade> propriedades;
}
