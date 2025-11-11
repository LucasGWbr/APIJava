package br.univates.api.controller;

import br.univates.api.config.JwtService;
import br.univates.api.dtos.AuthenticationResponseDTO;
import br.univates.api.dtos.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final br.univates.api.repositories.userRespository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody LoginRequestDTO request
    ) {
        // 1. AUTENTICAR
        // O Spring Security usa o AuthenticationManager para validar o usuário e senha.
        // Se as credenciais estiverem erradas, ele vai disparar uma exceção (BadCredentialsException)
        // e o método não continuará.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), // ou getUsername()
                        request.getPassword()
                )
        );

        // 2. GERAR O TOKEN
        // Se chegou aqui, a autenticação foi bem-sucedida.
        // Buscamos o usuário para que o JwtService possa usar seus dados (como email/ID/roles)
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // Já sabemos que ele existe
        // Gera o token JWT
        String jwtToken = jwtService.generateToken(user); // 'user' deve implementar UserDetails

        // 3. RETORNAR O TOKEN
        return ResponseEntity.ok(new AuthenticationResponseDTO(jwtToken,user.getName(),user.getEmail(),user.getId()));
    }

    // ... (você provavelmente também terá um endpoint /register)
}
