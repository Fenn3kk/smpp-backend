package br.ufsm.smpp.model.usuario;

import br.ufsm.smpp.model.propriedade.Propriedade;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    // --- Métodos da Interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna o papel do usuário no formato que o Spring Security entende.
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        // Usamos o e-mail como username para autenticação.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assume que as contas nunca expiram.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assume que as contas nunca são bloqueadas.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assume que as credenciais (senha) nunca expiram.
    }

    @Override
    public boolean isEnabled() {
        return true; // Assume que todas as contas estão sempre ativadas.
    }

    public enum TipoUsuario {
        ADMIN, COMUM
    }
}