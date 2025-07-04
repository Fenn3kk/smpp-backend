package br.ufsm.smpp.security;

import br.ufsm.smpp.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Componente utilitário para manipulação de JSON Web Tokens (JWT).
 * Responsável por gerar, validar e extrair informações de tokens.
 */
@Component
public class JwtUtil {

    /**
     * Chave secreta usada para assinar e verificar os tokens JWT.
     * O valor é injetado a partir da propriedade 'jwt.secret' no arquivo de configuração.
     */
    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Gera um token JWT para um usuário específico.
     * O token inclui o tipo de usuário como uma 'claim' (escopo) e tem validade de 24 horas.
     *
     * @param usuario O objeto Usuario para o qual o token será gerado.
     * @return Uma string representando o token JWT compacto.
     */
    public String gerarToken(Usuario usuario) {
        long umDiaEmMillis = 1000 * 60 * 60 * 24;

        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", usuario.getTipoUsuario().name());

        return Jwts.builder()
                .setClaims(claims) // Adiciona as claims personalizadas
                .setSubject(usuario.getEmail()) // Define o "assunto" do token como o email do usuário
                .setIssuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + umDiaEmMillis)) // Define a data de expiração (24 horas)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina o token com a chave e algoritmo
                .compact(); // Constrói e serializa o token
    }

    /**
     * Gera um token JWT para um e-mail específico, sem claims adicionais.
     * Útil para cenários como redefinição de senha. O token tem validade de 24 horas.
     *
     * @param email O e-mail a ser usado como "assunto" do token.
     * @return Uma string representando o token JWT compacto.
     */
    public String gerarToken(String email) {
        long umDiaEmMillis = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + umDiaEmMillis))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrai o email (claim 'subject') de um token JWT.
     *
     * @param token O token JWT.
     * @return O email contido no token.
     */
    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    /**
     * Verifica se um token JWT é válido para um determinado UserDetails.
     * A validação consiste em verificar se o e-mail no token corresponde ao username
     * do UserDetails e se o token não expirou.
     *
     * @param token O token JWT a ser validado.
     * @param userDetails Os detalhes do usuário para comparação.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extrairEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifica se um token JWT expirou.
     *
     * @param token O token JWT.
     * @return true se a data de expiração do token for anterior à data atual, false caso contrário.
     */
    private boolean isTokenExpired(String token) {
        return extrairExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração de um token JWT.
     *
     * @param token O token JWT.
     * @return A data de expiração (Date).
     */
    private Date extrairExpiration(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    /**
     * Função genérica para extrair uma claim específica de um token.
     * Utiliza uma função resolver para aplicar a lógica de extração desejada.
     *
     * @param token O token JWT.
     * @param claimsResolver Uma função que recebe um objeto Claims e retorna a claim desejada.
     * @param <T> O tipo da claim a ser retornada.
     * @return A claim extraída.
     */
    public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrairAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai todas as claims de um token JWT.
     * Este método faz o parse do token e verifica sua assinatura.
     *
     * @param token O token JWT.
     * @return Um objeto Claims contendo todos os dados do payload do token.
     */
    private Claims extrairAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gera a chave de assinatura (Key) a partir da string secreta.
     * A chave é gerada usando o algoritmo HMAC-SHA.
     *
     * @return A chave (Key) para assinar e verificar tokens.
     */
    private Key getSignInKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}