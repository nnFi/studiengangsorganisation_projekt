package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.repository.PruefungsordnungRepository;

/**
 * Service-Klasse für die Verwaltung von Prüfungsordnungen.
 */
@Service
public class PruefungsordnungService {
    @Autowired
    PruefungsordnungRepository pruefungsordnungRepository;

    /**
     * Konstruktor für die PruefungsordnungService-Klasse.
     */
    public PruefungsordnungService() {
    }

    /**
     * Holt alle Prüfungsordnungen.
     * @return Eine Liste aller vorhandenen Prüfungsordnungen.
     */
    public List<Pruefungsordnung> getPruefungsordnungen() {
        return pruefungsordnungRepository.findAll();
    }

    /**
     * Holt eine Prüfungsordnung anhand ihrer ID.
     * @param id Die ID der Prüfungsordnung.
     * @return Ein Optional, das die gefundene Prüfungsordnung enthält, falls vorhanden.
     */
    public Optional<Pruefungsordnung> getPruefungsordnung(Long id) {
        return pruefungsordnungRepository.findById(id);
    }

    /**
     * Speichert eine Prüfungsordnung und aktualisiert die Änderungen.
     * @param pruefungsordnung Die Prüfungsordnung, die gespeichert werden soll.
     * @return Die gespeicherte und aktualisierte Prüfungsordnung.
     */
    public Pruefungsordnung saveAndFlush(Pruefungsordnung pruefungsordnung) {
        return pruefungsordnungRepository.saveAndFlush(pruefungsordnung);
    }

    /**
     * Fügt Testdaten für eine Prüfungsordnung ein.
     * @param studiengang Der Studiengang, zu dem die Prüfungsordnung gehört.
     * @return Die erstellte Prüfungsordnung mit Testdaten.
     */
    public Pruefungsordnung insertTestData(Studiengang studiengang) {
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setFreigegeben(false);
        pruefungsordnung.setVersion("1");
        pruefungsordnung.setAuslaufend(false);
        pruefungsordnung.setStudiengang(studiengang);

        pruefungsordnungRepository.saveAndFlush(pruefungsordnung);

        return pruefungsordnung;
    }
}