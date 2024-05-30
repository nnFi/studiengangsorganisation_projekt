package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.repository.MitarbeiterRepository;

/**
 * Service-Klasse für die Verwaltung von Mitarbeitern.
 */
@Service
public class MitarbeiterService {

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    /**
     * Konstruktor für die MitarbeiterService-Klasse.
     */
    public MitarbeiterService() {

    }

    /**
     * Holt einen Mitarbeiter anhand seiner ID.
     * @param id Die ID des Mitarbeiters.
     * @return Ein Optional, das den gefundenen Mitarbeiter enthält, falls vorhanden.
     */
    public Optional<Mitarbeiter> getMitarbeiter(Long id) {
        return mitarbeiterRepository.findById(id);
    }

    /**
     * Holt alle Mitarbeiter.
     * @return Eine Liste aller vorhandenen Mitarbeiter.
     */
    public List<Mitarbeiter> getMitarbeiter() {
        return mitarbeiterRepository.findAll();
    }

    /**
     * Speichert einen Mitarbeiter und aktualisiert die Änderungen.
     * @param mitarbeiter Der Mitarbeiter, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Mitarbeiter.
     */
    public Mitarbeiter saveAndFlush(Mitarbeiter mitarbeiter) {
        return mitarbeiterRepository.saveAndFlush(mitarbeiter);
    }

    /**
     * Fügt Testdaten für einen Mitarbeiter ein.
     * @return Der erstellte Mitarbeiter mit Testdaten.
     */
    public Mitarbeiter insertTestData() {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname("Max");
        mitarbeiter.setNachname("Mustermann");

        mitarbeiterRepository.saveAndFlush(mitarbeiter);

        return mitarbeiter;
    }
}