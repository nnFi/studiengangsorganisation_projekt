package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MitarbeiterRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Mitarbeiter, Long> {

    Optional<com.projekt.studiengangsorganisation.entity.Mitarbeiter> findByUsername(String username);

}
