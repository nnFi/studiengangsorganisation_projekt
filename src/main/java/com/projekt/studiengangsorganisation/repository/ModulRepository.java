package com.projekt.studiengangsorganisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ein Repository für die Entität Modul.
 */
@Repository
public interface ModulRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Modul, Long> {

}
