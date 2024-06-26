package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.repository.ModulRepository;

/**
 * Service-Klasse für die Verwaltung von Modulen.
 */
@Service
public class ModulService {

    @Autowired
    ModulRepository modulRepository;

    /**
     * Konstruktor für die ModulService-Klasse.
     */
    public ModulService() {

    }

    /**
     * Holt alle Module.
     * 
     * @return Eine Liste aller vorhandenen Module.
     */
    public List<Modul> getModule() {
        return modulRepository.findAll();
    }

    /**
     * Holt ein Modul anhand seiner ID.
     * 
     * @param id Die ID des Moduls.
     * @return Ein Optional, das das gefundene Modul enthält, falls vorhanden.
     */
    public Optional<Modul> getModul(Long id) {
        return modulRepository.findById(id);
    }

    /**
     * Speichert ein Modul und aktualisiert die Änderungen.
     * 
     * @param modul Das Modul, das gespeichert werden soll.
     * @return Das gespeicherte und aktualisierte Modul.
     */
    public Modul saveAndFlush(Modul modul) {
        return modulRepository.saveAndFlush(modul);
    }

    /**
     * Diese Methode gibt eine Liste von Modulen zurück, die durch ihre IDs
     * identifiziert werden.
     *
     * @param list Eine Liste von Modul-IDs, für die Module abgerufen werden sollen.
     * @return Eine Liste von Modulen, die den angegebenen IDs entsprechen.
     */
    public List<Modul> getModuleByIds(List<Long> list) {
        return modulRepository.findAllById(list);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        modulRepository.deleteAll();
    }
}