package br.ufsm.smpp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

/**
 * Representa um arquivo de foto associado a uma ocorrência específica.
 * Esta entidade armazena metadados sobre a imagem, como seu nome e
 * o caminho para o arquivo no servidor, e a vincula à ocorrência correspondente.
 */
@Entity
@Table(name = "foto_ocorrencia")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FotoOcorrencia {

    /**
     * Identificador único do registro da foto, gerado automaticamente como um UUID.
     * Atua como a chave primária na tabela 'foto_ocorrencia'.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * A ocorrência à qual esta foto pertence.
     * É uma relação muitos-para-um (várias fotos podem pertencer a uma ocorrência).
     * - {@code fetch = FetchType.LAZY}: A ocorrência só será carregada do banco de dados quando for explicitamente acessada.
     * - {@code @JsonIgnore}: Evita que o objeto Ocorrencia seja serializado em JSON, prevenindo loops infinitos.
     * - {@code @ToString.Exclude} e {@code @EqualsAndHashCode.Exclude}: Evitam recursão ao gerar os métodos toString() e equals/hashCode.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ocorrencia_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ocorrencia ocorrencia;

    /**
     * O nome original do arquivo da foto (ex: "imagem_do_local.jpg").
     * Pode ser útil para exibição ou referência.
     */
    @Column(name = "nome")
    private String nome;

    /**
     * O caminho ou nome único do arquivo armazenado no servidor.
     * Este campo é essencial para localizar e servir a imagem.
     * Não pode ser nulo ou vazio.
     */
    @NotBlank
    @Column(name = "caminho", nullable = false)
    private String caminho;
}