package br.ufsm.smpp.model.atividade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
@Table(name = "Atividade")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    @NonNull
    @NotNull
    private String nome;
}

