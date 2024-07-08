package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.NutzerRepository;

/**
 * Service-Klasse für die Verwaltung von Nutzern.
 * 
 * @author Erkan Yüzer
 */
@Service
public class NutzerService {

    @Autowired
    NutzerRepository nutzerRepository;

    /**
     * Konstruktor für die NutzerService-Klasse.
     */
    public NutzerService() {

    }

    /**
     * Holt alle Nutzer.
     * 
     * @return Eine Liste aller vorhandenen Nutzer.
     */
    public List<Nutzer> getNutzer() {
        return nutzerRepository.findAll();
    }

    /**
     * Holt einen Nutzer anhand seiner ID.
     * 
     * @param id Die ID des Nutzers.
     * @return Ein Optional, das den gefundenen Nutzer enthält, falls vorhanden.
     */
    public Optional<Nutzer> getNutzer(String id) {
        return nutzerRepository.findById(Long.parseLong(id));
    }

    /**
     * Holt einen Nutzer anhand seines Benutzernamens.
     * 
     * @param username Der Benutzername des Nutzers.
     * @return Ein Optional, das den gefundenen Nutzer enthält, falls vorhanden.
     */
    public Optional<Nutzer> getNutzerByUsername(String username) {
        return nutzerRepository.findByUsername(username);
    }

    /**
     * Speichert einen Nutzer und aktualisiert die Änderungen.
     * 
     * @param nutzer Der Nutzer, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Nutzer.
     */
    public Nutzer saveAndFlush(Nutzer nutzer) {
        return nutzerRepository.saveAndFlush(nutzer);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        nutzerRepository.deleteAll();
    }
}