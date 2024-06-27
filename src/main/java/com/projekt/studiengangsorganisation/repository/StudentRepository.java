package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Student.
 * 
 * @author Erkan Yüzer
 */
@Repository
public interface StudentRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Student, Long> {

    /**
     * Sucht einen Student anhand des Benutzernamens.
     * 
     * @param username Der Benutzername, nach dem gesucht wird.
     * @return Eine optionale Instanz von Student, die den gefundenen Student
     *         enthält, falls vorhanden.
     */
    Optional<com.projekt.studiengangsorganisation.entity.Student> findByUsername(String username);

}
