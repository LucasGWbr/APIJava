package br.univates.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// NOTA: Você precisará criar o 'JwtService' (próximo passo)
// e um 'UserDetailsService' (que busca o usuário no banco)

@Component // Torna este filtro um bean gerenciado pelo Spring
@RequiredArgsConstructor // Para injetar os serviços via construtor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Seu serviço que valida o token
    private final UserDetailsService userDetailsService; // Serviço do Spring que busca o usuário

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail; // Ou username, etc.

        // 1. Verifica se o cabeçalho existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Se não tiver, continua o fluxo normal (provavelmente será barrado depois)
            return;
        }

        // 2. Extrai o token
        jwt = authHeader.substring(7); // "Bearer " tem 7 caracteres

        // 3. Extrai o usuário do token
        userEmail = jwtService.extractUsername(jwt); // Você precisa criar o JwtService

        // 4. Se temos um usuário E ele ainda não está autenticado no contexto de segurança
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Busca o usuário no banco de dados
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Cria a autenticação e a coloca no contexto de segurança
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Não usamos credenciais (senha) aqui
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}