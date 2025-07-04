package br.ufsm.smpp.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro de autenticação JWT que intercepta todas as requisições HTTP.
 * Este filtro é executado uma vez por requisição para verificar a presença
 * e a validade de um token JWT no cabeçalho 'Authorization'.
 * Se um token válido for encontrado, ele autentica o usuário e o define
 * no contexto de segurança do Spring (SecurityContextHolder).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Utilitário para criar, validar e extrair informações de tokens JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Serviço do Spring Security para carregar os detalhes do usuário (por exemplo, do banco de dados).
     */
    private final UserDetailsService userDetailsService;

    /**
     * Método principal do filtro que processa cada requisição.
     *
     * @param request     O objeto da requisição HTTP.
     * @param response    O objeto da resposta HTTP.
     * @param filterChain A cadeia de filtros do Spring Security, para passar a requisição adiante.
     * @throws ServletException Se ocorrer um erro de servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Extrai o cabeçalho 'Authorization' da requisição.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verifica se o cabeçalho existe e se começa com "Bearer ".
        // Se não, a requisição é passada para o próximo filtro sem autenticação.
        // Isso permite que endpoints públicos (como /auth/login) sejam acessados.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token JWT, removendo o prefixo "Bearer ".
        jwt = authHeader.substring(7);
        // 3. Extrai o e-mail (subject) do usuário a partir do token.
        userEmail = jwtUtil.extrairEmail(jwt);

        // 4. Verifica se o e-mail foi extraído e se não há uma autenticação ativa no contexto de segurança.
        // A segunda verificação evita reprocessar a autenticação para uma requisição já autenticada.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário a partir do banco de dados usando o e-mail.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token: verifica se a assinatura é válida e se não está expirado.
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // Se o token for válido, cria um objeto de autenticação.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) são nulas, pois a autenticação é via token.
                        userDetails.getAuthorities()
                );
                // Adiciona detalhes da requisição web (como IP e sessão) ao objeto de autenticação.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Define o objeto de autenticação no SecurityContextHolder.
                // A partir deste ponto, o usuário é considerado autenticado pelo Spring Security.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 7. Passa a requisição (e a resposta) para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }
}