package br.ufsm.smpp.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "incidente")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Incidente {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;
}
