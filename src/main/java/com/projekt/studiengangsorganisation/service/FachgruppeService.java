package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.repository.FachgruppeRepository;

/**
 * Service-Klasse für die Verwaltung von Fachgruppen.
 */
@Service
public class FachgruppeService {

    @Autowired
    FachgruppeRepository fachgruppeRepository;

    /**
     * Konstruktor für die FachgruppeService-Klasse.
     */
    public FachgruppeService() {

    }

    /**
     * Holt alle Fachgruppen.
     * 
     * @return Eine Liste aller vorhandenen Fachgruppen.
     */
    public List<Fachgruppe> getFachgruppen() {
        return fachgruppeRepository.findAll();
    }

    /**
     * Holt eine Fachgruppe anhand ihrer ID.
     * 
     * @param id Die ID der Fachgruppe.
     * @return Ein Optional, das die gefundene Fachgruppe enthält, falls vorhanden.
     */
    public Optional<Fachgruppe> getFachgruppe(Long id) {
        return fachgruppeRepository.findById(id);
    }

    /**
     * Holt eine Fachgruppe anhand ihres Namens.
     * 
     * @param name Der Name der Fachgruppe.
     * @return Ein Optional, das die gefundene Fachgruppe enthält, falls vorhanden.
     */
    public Optional<Fachgruppe> getFachgruppeByName(String name) {
        return fachgruppeRepository.findByName(name);
    }

    /**
     * Holt eine Fachgruppe anhand ihres Kürzels.
     * 
     * @param kuerzel Das Kürzel der Fachgruppe.
     * @return Ein Optional, das die gefundene Fachgruppe enthält, falls vorhanden.
     */
    public Optional<Fachgruppe> getFachgruppeByKuerzel(String kuerzel) {
        return fachgruppeRepository.findByKuerzel(kuerzel);
    }

    /**
     * Speichert eine Fachgruppe und aktualisiert die Änderungen.
     * 
     * @param fachgruppe Die Fachgruppe, die gespeichert werden soll.
     * @return Die gespeicherte und aktualisierte Fachgruppe.
     */
    public Fachgruppe saveAndFlush(Fachgruppe fachgruppe) {
        return fachgruppeRepository.saveAndFlush(fachgruppe);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        fachgruppeRepository.deleteAll();
    }
}