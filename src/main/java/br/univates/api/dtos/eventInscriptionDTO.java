package br.univates.api.dtos;

import java.time.OffsetDateTime;

public record eventInscriptionDTO(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, String location, String certificateTemplatePath, int inscriptionId, String status ,int eventId, String userName) {

}
