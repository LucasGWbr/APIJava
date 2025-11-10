package br.univates.api.repositories;

import br.univates.api.dtos.eventInscriptionDTO;
import br.univates.api.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
//String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, String location, String certificateTemplatePath
public interface eventRepository extends JpaRepository<Events, Integer> {
    @Query("SELECT new br.univates.api.dtos.eventInscriptionDTO(e.name,e.description,e.startDate,e.endDate,e.location,e.certificateTemplatePath, i.inscriptionId, i.status, e.eventId, u.name) FROM inscription i JOIN Events e ON e.eventId = i.eventId JOIN users u ON u.user_id = i.userId WHERE u.user_id = :userId AND (i.status = 'INSCRIPT' OR i.status = 'PRESENCE')")
    List<eventInscriptionDTO> findAllByUserId(@Param("userId") int userId);
}
