package com.projekt.studiengangsorganisation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekt.studiengangsorganisation.entity.Modul;

/**
 * Ein Repository für die Entität Modul.
 * 
 * @author Paul Rakow
 */
@Repository
public interface ModulRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Modul, Long> {

    List<Modul> findByModulgruppe_IdIn(List<Long> modulgruppeIds);
}
