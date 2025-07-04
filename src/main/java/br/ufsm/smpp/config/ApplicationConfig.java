package br.ufsm.smpp.config;

import br.ufsm.smpp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração para os beans relacionados à segurança da aplicação.
 * Define os componentes essenciais para o funcionamento do Spring Security,
 * como o provedor de autenticação, o gerenciador de autenticação e o codificador de senhas.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    /**
     * Repositório para acesso aos dados dos usuários, injetado via construtor pelo Lombok.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Define o serviço que carrega os detalhes do usuário a partir do banco de dados.
     * O Spring Security utiliza este bean para encontrar um usuário pelo seu nome de usuário (neste caso, o e-mail).
     *
     * @return Uma implementação de UserDetailsService que busca o usuário pelo e-mail.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));
    }

    /**
     * Configura o provedor de autenticação (AuthenticationProvider).
     * Este componente é responsável por verificar as credenciais do usuário.
     * Utiliza o DaoAuthenticationProvider, que integra o UserDetailsService (para buscar o usuário)
     * e o PasswordEncoder (para comparar as senhas).
     *
     * @return Um AuthenticationProvider configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Define o serviço para buscar os detalhes do usuário.
        authProvider.setUserDetailsService(userDetailsService());
        // Define o codificador de senhas para verificar a senha.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager como um bean no contexto do Spring.
     * O AuthenticationManager é a principal interface do Spring Security para processar
     * uma solicitação de autenticação.
     *
     * @param config A configuração de autenticação do Spring.
     * @return O AuthenticationManager gerenciado pelo Spring.
     * @throws Exception se houver um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define o bean para a codificação de senhas.
     * Utiliza o BCrypt, um algoritmo de hashing forte e adaptativo, o padrão recomendado
     * para armazenar senhas de forma segura.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}