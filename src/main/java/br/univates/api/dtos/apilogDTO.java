package br.univates.api.dtos;

public record apilogDTO(String method, String endpoint, int status, String response, String request, long response_time) {
}
