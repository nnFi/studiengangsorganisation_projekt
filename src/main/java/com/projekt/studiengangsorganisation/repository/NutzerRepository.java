package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Nutzer.
 * 
 * @author Erkan Yüzer
 */
@Repository
public interface NutzerRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Nutzer, Long> {

    /**
     * Sucht einen Nutzer anhand des Benutzernamens.
     * 
     * @param username Der Benutzername, nach dem gesucht wird.
     * @return Eine optionale Instanz von Nutzer, die den gefundenen Nutzer enthält,
     *         falls vorhanden.
     */
    Optional<com.projekt.studiengangsorganisation.entity.Nutzer> findByUsername(String username);

}
