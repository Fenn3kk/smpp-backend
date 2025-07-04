package br.ufsm.smpp.config;

import br.ufsm.smpp.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração principal do Spring Security.
 * Responsável por definir a cadeia de filtros de segurança, regras de autorização,
 * gerenciamento de sessão e a integração do filtro de autenticação JWT.
 *
 * @Configuration Indica que esta é uma classe de configuração do Spring.
 * @EnableWebSecurity Habilita a integração do Spring Security com o Spring MVC.
 * @EnableMethodSecurity Habilita a segurança em nível de método (ex: @PreAuthorize).
 * @RequiredArgsConstructor Gera um construtor com os campos final, facilitando a injeção de dependência.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Filtro customizado para processar o token JWT em cada requisição.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Provedor de autenticação que define como os usuários são autenticados (ex: buscando no banco de dados).
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * Define e configura a cadeia de filtros de segurança (SecurityFilterChain) que será aplicada
     * às requisições HTTP.
     *
     * @param http O objeto HttpSecurity para construir a configuração de segurança.
     * @return A cadeia de filtros de segurança construída.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita a proteção contra Cross-Site Request Forgery (CSRF).
                // Comum em APIs RESTful stateless, onde a autenticação é feita por token a cada requisição.
                .csrf(csrf -> csrf.disable())

                // Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Permite o acesso a todos os endpoints que começam com "/auth/**" sem autenticação.
                        // Usado para login, cadastro, etc.
                        .requestMatchers("/auth/**").permitAll()
                        // Permite o acesso via método GET a arquivos na pasta de uploads.
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                        // Exige autenticação para qualquer outra requisição não especificada acima.
                        .anyRequest().authenticated()
                )

                // Configura o gerenciamento de sessão.
                // Define a política como STATELESS, o que significa que o servidor não criará ou usará sessões HTTP.
                // Cada requisição deve conter toda a informação necessária para ser processada (o token JWT).
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Registra o provedor de autenticação customizado.
                .authenticationProvider(authenticationProvider)

                // Adiciona o filtro de autenticação JWT (jwtAuthFilter) antes do filtro padrão de autenticação
                // de usuário e senha (UsernamePasswordAuthenticationFilter).
                // Isso garante que o token seja validado antes de qualquer outro processo de autenticação.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Constrói e retorna o objeto SecurityFilterChain.
        return http.build();
    }
}