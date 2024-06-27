package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Fachgruppe.
 * 
 * @author Bao Chau Kathi Doan
 */
@Repository
public interface FachgruppeRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Fachgruppe, Long> {

    Optional<com.projekt.studiengangsorganisation.entity.Fachgruppe> findByName(String name);

    Optional<com.projekt.studiengangsorganisation.entity.Fachgruppe> findByKuerzel(String kuerzel);

}
