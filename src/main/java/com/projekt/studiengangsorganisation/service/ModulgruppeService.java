package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.repository.ModulgruppeRepository;

/**
 * Service-Klasse für die Verwaltung von Modulgruppen.
 * 
 * @author Erkan Yüzer
 */
@Service
public class ModulgruppeService {

    @Autowired
    ModulgruppeRepository modulgruppeRepository;

    /**
     * Konstruktor für die ModulgruppeService-Klasse.
     */
    public ModulgruppeService() {

    }

    /**
     * Holt alle Modulgruppen.
     * 
     * @return Eine Liste aller vorhandenen Modulgruppen.
     */
    public List<Modulgruppe> getModulgruppen() {
        return modulgruppeRepository.findAll();
    }

    /**
     * Holt eine Modulgruppe anhand ihrer ID.
     * 
     * @param id Die ID der Modulgruppe.
     * @return Ein Optional, das die gefundene Modulgruppe enthält, falls vorhanden.
     */
    public Optional<Modulgruppe> getModulgruppe(Long id) {
        return modulgruppeRepository.findById(id);
    }

    /**
     * Speichert eine Modulgruppe und aktualisiert die Änderungen.
     * 
     * @param modulgruppe Die Modulgruppe, die gespeichert werden soll.
     * @return Die gespeicherte und aktualisierte Modulgruppe.
     */
    public Modulgruppe saveAndFlush(Modulgruppe modulgruppe) {
        return modulgruppeRepository.saveAndFlush(modulgruppe);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        modulgruppeRepository.deleteAll();
    }
}