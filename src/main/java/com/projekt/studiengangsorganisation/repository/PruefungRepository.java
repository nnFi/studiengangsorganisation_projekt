package com.projekt.studiengangsorganisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Pruefung.
 * 
 * @author Finn Plassmeier
 */
@Repository
public interface PruefungRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Pruefung, Long> {

}
