package br.univates.api.dtos;

import jakarta.persistence.Column;

import java.time.OffsetDateTime;

public record eventDTO(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, String location) {
}
