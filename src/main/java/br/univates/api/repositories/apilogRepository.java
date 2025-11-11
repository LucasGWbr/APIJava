package br.univates.api.repositories;

import br.univates.api.model.api_log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface apilogRepository extends JpaRepository<api_log, Integer> {
}
