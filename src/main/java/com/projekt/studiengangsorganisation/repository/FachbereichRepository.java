package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Fachbereich.
 */
@Repository
public interface FachbereichRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Fachbereich, Long> {

    Optional<com.projekt.studiengangsorganisation.entity.Fachbereich> findByName(String name);

}