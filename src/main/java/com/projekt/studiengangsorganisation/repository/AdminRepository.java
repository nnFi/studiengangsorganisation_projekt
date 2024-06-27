package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Admin.
 * 
 * @author Erkan Yüzer
 */
@Repository
public interface AdminRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Admin, Long> {

    /**
     * Sucht einen Admin anhand des Benutzernamens.
     * 
     * @param username Der Benutzername, nach dem gesucht wird.
     * @return Eine optionale Instanz von Admin, die den gefundenen Admin enthält,
     *         falls vorhanden.
     */
    Optional<com.projekt.studiengangsorganisation.entity.Admin> findByUsername(String username);

}
