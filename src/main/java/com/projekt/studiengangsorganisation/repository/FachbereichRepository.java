package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Fachbereich.
 * 
 * @author Erkan Yüzer
 */
@Repository
public interface FachbereichRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Fachbereich, Long> {

    /**
     * Findet einen Fachbereich anhand seines Namens.
     * 
     * @param name der Name des Fachbereichs
     * @return ein Optional, das den gefundenen Fachbereich enthält, oder ein leeres Optional, wenn kein Fachbereich mit dem gegebenen Namen gefunden wurde
     */
    Optional<com.projekt.studiengangsorganisation.entity.Fachbereich> findByName(String name);

}