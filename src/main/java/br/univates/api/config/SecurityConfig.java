package br.univates.api.config; // Crie um pacote de configuração se não tiver

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilita a configuração de CORS que definimos no bean mais abaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Desabilita o CSRF (Cross-Site Request Forgery) - comum em APIs stateless
                .csrf(csrf -> csrf.disable())

                // 3. Define as regras de autorização para os endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll() // Permite todas as requisições por enquanto. Ajuste conforme necessário.
                        .anyRequest().authenticated()
                );

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