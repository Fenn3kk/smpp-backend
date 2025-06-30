package br.ufsm.smpp.model.incidente;

import br.ufsm.smpp.model.ocorrencia.Ocorrencia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
