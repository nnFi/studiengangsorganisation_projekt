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
     * 
     * @return Eine Liste aller vorhandenen Prüfungsordnungen.
     */
    public List<Pruefungsordnung> getPruefungsordnungen() {
        return pruefungsordnungRepository.findAll();
    }

    /**
     * Holt eine Prüfungsordnung anhand ihrer ID.
     * 
     * @param id Die ID der Prüfungsordnung.
     * @return Ein Optional, das die gefundene Prüfungsordnung enthält, falls
     *         vorhanden.
     */
    public Optional<Pruefungsordnung> getPruefungsordnung(Long id) {
        return pruefungsordnungRepository.findById(id);
    }

    /**
     * Holt eine Prüfungsordnung anhand von Version und Studiengang.
     * 
     * @param version      Die Version der Prüfungsordnung.
     * @param studiengang   Der Studiengang der zur Prüfungsordnung gehört.
     * @return Ein Optional, das den gefundenen Studiengang enthält, falls
     *         vorhanden.
     */
    public Optional<Pruefungsordnung> getPruefungsordnung(String version, Studiengang studiengang) {
        return pruefungsordnungRepository.findByVersionAndStudiengang(version, studiengang);
    }

    /**
     * Speichert eine Prüfungsordnung und aktualisiert die Änderungen.
     * 
     * @param pruefungsordnung Die Prüfungsordnung, die gespeichert werden soll.
     * @return Die gespeicherte und aktualisierte Prüfungsordnung.
     */
    public Pruefungsordnung saveAndFlush(Pruefungsordnung pruefungsordnung) {
        return pruefungsordnungRepository.saveAndFlush(pruefungsordnung);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        pruefungsordnungRepository.deleteAll();
    }
}