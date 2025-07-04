package br.ufsm.smpp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Representa um usuário no sistema.
 * Esta entidade armazena as informações de login e os dados pessoais do usuário.
 * Implementa a interface {@link UserDetails} do Spring Security para se integrar
 * ao mecanismo de autenticação e autorização da aplicação.
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    /**
     * Identificador único do usuário, gerado automaticamente como um UUID.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /**
     * Nome completo do usuário.
     * Não pode ser nulo.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Endereço de e-mail do usuário.
     * É usado como nome de usuário para login e deve ser único no sistema.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Número de telefone do usuário.
     * Não pode ser nulo.
     */
    @Column(nullable = false)
    private String telefone;

    /**
     * Senha do usuário, armazenada de forma criptografada (hashed).
     * Não pode ser nula.
     */
    @Column(nullable = false)
    private String senha;

    /**
     * O tipo de perfil do usuário (ex: ADMIN, COMUM), que define suas permissões.
     * Armazenado como uma String no banco de dados.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    /**
     * Retorna as autorizações (perfis/roles) concedidas ao usuário.
     * O Spring Security usa esta coleção para controle de acesso. O prefixo "ROLE_" é uma convenção.
     *
     * @return Uma coleção contendo a autoridade do usuário (ex: "ROLE_ADMIN").
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    /**
     * Retorna a senha usada para autenticar o usuário.
     *
     * @return A senha criptografada do usuário.
     */
    @Override
    public String getPassword() {
        return this.senha;
    }

    /**
     * Retorna o nome de usuário usado para autenticar o usuário. Neste caso, é o e-mail.
     *
     * @return O e-mail do usuário.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Enumeração que define os tipos de perfis de usuário no sistema.
     */
    public enum TipoUsuario {
        /**
         * Perfil de administrador com acesso total ao sistema.
         */
        ADMIN,
        /**
         * Perfil de usuário padrão com acesso a tudo menos cadastrar outro usuário.
         */
        COMUM
    }
}