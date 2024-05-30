package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.repository.FachbereichRepository;

/**
 * Service-Klasse für die Verwaltung von Fachbereichen.
 */
@Service
public class FachbereichService {
    @Autowired
    FachbereichRepository fachbereichRepository;

    /**
     * Konstruktor für die FachbereichService-Klasse.
     */
    public FachbereichService() {

    }

    /**
     * Holt alle Fachbereiche.
     * @return Eine Liste aller vorhandenen Fachbereiche.
     */
    public List<Fachbereich> getFachbereiche() {
        return fachbereichRepository.findAll();
    }

    /**
     * Holt einen Fachbereich anhand seiner ID.
     * @param id Die ID des Fachbereichs.
     * @return Ein Optional, das den gefundenen Fachbereich enthält, falls vorhanden.
     */
    public Optional<Fachbereich> getFachbereich(Long id) {
        return fachbereichRepository.findById(id);
    }

    /**
     * Speichert einen Fachbereich und aktualisiert die Änderungen.
     * @param fachbereich Der Fachbereich, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Fachbereich.
     */
    public Fachbereich saveAndFlush(Fachbereich fachbereich) {
        return fachbereichRepository.saveAndFlush(fachbereich);
    }

    /**
     * Fügt Testdaten für einen Fachbereich ein.
     * @param referent Der Referent des Fachbereichs.
     * @param stellvertreter Der Stellvertreter des Fachbereichs.
     * @return Der erstellte Fachbereich mit Testdaten.
     */
    public Fachbereich insertTestData(Mitarbeiter referent, Mitarbeiter stellvertreter) {
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName("Wirtschaft");
        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);

        fachbereichRepository.saveAndFlush(fachbereich);

        return fachbereich;
    }
}