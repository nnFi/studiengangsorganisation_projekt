package com.projekt.studiengangsorganisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Modulgruppe.
 * 
 * @author Erkan Yüzer
 */
@Repository
public interface ModulgruppeRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Modulgruppe, Long> {

}
