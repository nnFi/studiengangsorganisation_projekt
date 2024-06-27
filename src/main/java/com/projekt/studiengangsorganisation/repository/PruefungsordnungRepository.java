package com.projekt.studiengangsorganisation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Studiengang;

/**
 * Ein Repository für die Entität Priefungsordnung.
 * 
 * @author Finn Plassmeier
 */
@Repository
public interface PruefungsordnungRepository
                extends JpaRepository<com.projekt.studiengangsorganisation.entity.Pruefungsordnung, Long> {

        /**
         * Findet eine Pruefungsordnung anhand ihrer Version und des zugehörigen Studiengangs.
         * 
         * @param version die Version der Pruefungsordnung
         * @param studiengang der Studiengang, zu dem die Pruefungsordnung gehört
         * @return ein Optional, das die gefundene Pruefungsordnung enthält, oder ein leeres Optional, wenn keine Pruefungsordnung mit der gegebenen Version und dem Studiengang gefunden wurde
         */
        Optional<com.projekt.studiengangsorganisation.entity.Pruefungsordnung> findByVersionAndStudiengang(
                        String version,
                        Studiengang studiengang);

        /**
         * Findet alle Pruefungsordnungen, die zu den angegebenen Studiengang-IDs gehören.
         * 
         * @param studiengangIds die IDs der Studiengänge
         * @return eine Liste der gefundenen Pruefungsordnungen
         */
        List<Pruefungsordnung> findByStudiengang_IdIn(List<Long> studiengangIds);
}
