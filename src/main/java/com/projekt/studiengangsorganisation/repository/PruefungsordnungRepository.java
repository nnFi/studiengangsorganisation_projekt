package com.projekt.studiengangsorganisation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Studiengang;

/**
 * Ein Repository für die Entität Priefungsordnung.
 */
@Repository
public interface PruefungsordnungRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Pruefungsordnung, Long> {

    Optional<com.projekt.studiengangsorganisation.entity.Pruefungsordnung> findByVersionAndStudiengang(String version,
            Studiengang studiengang);

    List<Pruefungsordnung> findByStudiengang_IdIn(List<Long> studiengangIds);
}
