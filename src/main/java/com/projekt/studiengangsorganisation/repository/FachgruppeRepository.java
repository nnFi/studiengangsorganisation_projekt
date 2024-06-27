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

    /**
     * Findet eine Fachgruppe anhand ihres Namens.
     * 
     * @param name der Name der Fachgruppe
     * @return ein Optional, das die gefundene Fachgruppe enthält, oder ein leeres Optional, wenn keine Fachgruppe mit dem gegebenen Namen gefunden wurde
     */
    Optional<com.projekt.studiengangsorganisation.entity.Fachgruppe> findByName(String name);

    /**
     * Findet eine Fachgruppe anhand ihres Kürzels.
     * 
     * @param kuerzel das Kürzel der Fachgruppe
     * @return ein Optional, das die gefundene Fachgruppe enthält, oder ein leeres Optional, wenn keine Fachgruppe mit dem gegebenen Kürzel gefunden wurde
     */
    Optional<com.projekt.studiengangsorganisation.entity.Fachgruppe> findByKuerzel(String kuerzel);

}
