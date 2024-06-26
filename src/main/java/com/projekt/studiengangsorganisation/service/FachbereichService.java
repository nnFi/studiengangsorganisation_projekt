package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
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
     * 
     * @return Eine Liste aller vorhandenen Fachbereiche.
     */
    public List<Fachbereich> getFachbereiche() {
        return fachbereichRepository.findAll();
    }

    /**
     * Holt einen Fachbereich anhand seiner ID.
     * 
     * @param id Die ID des Fachbereichs.
     * @return Ein Optional, das den gefundenen Fachbereich enthält, falls
     *         vorhanden.
     */
    public Optional<Fachbereich> getFachbereich(Long id) {
        return fachbereichRepository.findById(id);
    }

    /**
     * Holt einen Fachbereich anhand seiner ID.
     * 
     * @param name Der Name des Fachbereichs.
     * @return Ein Optional, das den gefundenen Fachbereich enthält, falls
     *         vorhanden.
     */
    public Optional<Fachbereich> getFachbereichByName(String name) {
        return fachbereichRepository.findByName(name);
    }

    /**
     * Speichert einen Fachbereich und aktualisiert die Änderungen.
     * 
     * @param fachbereich Der Fachbereich, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Fachbereich.
     */
    public Fachbereich saveAndFlush(Fachbereich fachbereich) {
        return fachbereichRepository.saveAndFlush(fachbereich);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        fachbereichRepository.deleteAll();
    }
}