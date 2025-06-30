package br.ufsm.smpp.model.ocorrencia.foto_ocorrencia;

import br.ufsm.smpp.model.ocorrencia.Ocorrencia;
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
@RequiredArgsConstructor
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
    private String nome; // Nome original do arquivo, opcional

    @NotBlank
    @Column(name = "caminho", nullable = false)
    private String caminho; // Caminho/nome único no servidor
}