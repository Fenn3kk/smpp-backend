package br.ufsm.smpp.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "atividade")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Atividade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}

