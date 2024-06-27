package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Mitarbeiter.
 * 
 * @author Erkan Yüzer
 */
@Repository
public interface MitarbeiterRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Mitarbeiter, Long> {

    /**
     * Sucht einen Mitarbeiter anhand des Benutzernamens.
     * 
     * @param username Der Benutzername, nach dem gesucht wird.
     * @return Eine optionale Instanz von Mitarbeiter, die den gefundenen
     *         Mitarbeiter enthält, falls vorhanden.
     */
    Optional<com.projekt.studiengangsorganisation.entity.Mitarbeiter> findByUsername(String username);

}
