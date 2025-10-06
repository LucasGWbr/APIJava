package br.univates.api.repositories;

import br.univates.api.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface eventRepository extends JpaRepository<Events, Integer> {

}
