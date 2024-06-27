package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekt.studiengangsorganisation.entity.Abschluss;

/**
 * Ein Repository für die Entität Studiengang.
 * 
 * @author Bao Chau Kathi Doan
 */
@Repository
public interface StudiengangRepository
                extends JpaRepository<com.projekt.studiengangsorganisation.entity.Studiengang, Long> {

        /**
         * Findet einen Studiengang anhand seines Namens und Abschlusses.
         * 
         * @param name der Name des Studiengangs
         * @param abschluss der Abschluss des Studiengangs
         * @return ein Optional, das den gefundenen Studiengang enthält, oder ein leeres Optional, wenn kein Studiengang mit dem gegebenen Namen und Abschluss gefunden wurde
         */
        Optional<com.projekt.studiengangsorganisation.entity.Studiengang> findByNameAndAbschluss(String name,
                        Abschluss abschluss);

}
