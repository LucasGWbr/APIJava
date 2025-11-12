package br.univates.api.dtos;

public record apilogDTO(String method, String endpoint, int status, long response_time) {
}
