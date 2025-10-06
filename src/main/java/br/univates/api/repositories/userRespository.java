package br.univates.api.repositories;


import br.univates.api.model.users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRespository extends JpaRepository<users, Integer> {

    Optional<users> findByEmail(String email);
}
