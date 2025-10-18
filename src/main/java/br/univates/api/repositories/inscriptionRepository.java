package br.univates.api.repositories;

import br.univates.api.model.inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface inscriptionRepository extends JpaRepository<inscription, Integer>{
    List<inscription> findAllByuserId(int id);
    boolean existsByEventIdAndUserId(int eventId, int userId);
    inscription findByEventIdAndUserId(int eventId, int userId);
}
