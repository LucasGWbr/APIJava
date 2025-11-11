package br.univates.api.dtos;// DTO para enviar o token de volta
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor // Para o construtor com o token
public class AuthenticationResponseDTO {
    private final String token;
    private final String name;
    private final String email;
    private final int id;
}