package br.ufsm.smpp.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "foto_ocorrencia")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FotoOcorrencia {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ocorrencia_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ocorrencia ocorrencia;

    @Column(name = "nome")
    private String nome;

    @NotBlank
    @Column(name = "caminho", nullable = false)
    private String caminho;
}