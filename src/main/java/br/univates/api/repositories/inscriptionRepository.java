package br.univates.api.repositories;

import br.univates.api.model.inscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface inscriptionRepository extends JpaRepository<inscription, Integer>{
}
