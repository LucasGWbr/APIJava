package br.univates.api.config; // Crie um pacote de configuração se não tiver

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // NOVO: Injete o filtro que você acabou de criar
    private final JwtAuthenticationFilter jwtAuthFilter;
    // NOVO: Você também precisará injetar um AuthenticationProvider
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // CSRF desabilitado para APIs stateless

                // NOVO: Define as regras de autorização
                .authorizeHttpRequests(authorize -> authorize
                        // ATENÇÃO AQUI:
                        // Libere seus endpoints públicos (login, registrar)
                        .requestMatchers("/auth").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/events").permitAll()

                        // Exija autenticação para todo o resto
                        .anyRequest().authenticated()
                )

                // NOVO: Configura o gerenciamento de sessão
                .sessionManagement(session -> session
                        // Define a política de criação de sessão como STATELESS (sem estado)
                        // O Spring Security não criará sessões HTTP
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // NOVO: Define o provedor de autenticação
                .authenticationProvider(authenticationProvider)

                // NOVO: Adiciona o seu filtro JWT *antes* do filtro padrão de usuário/senha
                // Isso garante que sua lógica de token seja executada primeiro
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 4. Define a origem permitida (o endereço do seu front-end)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // 5. Define os métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 6. Define os cabeçalhos permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));

        // 7. Permite o envio de credenciais (como cookies), se necessário
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a configuração a todos os paths da sua aplicação
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}