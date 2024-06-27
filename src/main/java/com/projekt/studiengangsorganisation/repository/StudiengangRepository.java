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

        Optional<com.projekt.studiengangsorganisation.entity.Studiengang> findByNameAndAbschluss(String name,
                        Abschluss abschluss);

}
