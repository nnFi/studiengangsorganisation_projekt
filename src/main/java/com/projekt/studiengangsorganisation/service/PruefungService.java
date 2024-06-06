package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.repository.PruefungRepository;

/**
 * Service-Klasse für die Verwaltung von Prüfungen.
 */
@Service
public class PruefungService {

    @Autowired
    PruefungRepository pruefungRepository;

    /**
     * Konstruktor für die PruefungService-Klasse.
     */
    public PruefungService() {
    }

    /**
     * Holt eine Prüfung anhand ihrer ID.
     * 
     * @param id Die ID der Prüfung.
     * @return Ein Optional, das die gefundene Prüfung enthält, falls vorhanden.
     */
    public Optional<Pruefung> getPruefung(String id) {
        return pruefungRepository.findById(Long.parseLong(id));
    }

    /**
     * Holt alle Prüfungen.
     * 
     * @return Eine Liste aller vorhandenen Prüfungen.
     */
    public List<Pruefung> getPruefungen() {
        return pruefungRepository.findAll();
    }

    /**
     * Holt eine Liste von Prüfungen anhand ihrer IDs.
     * 
     * @param ids Die Liste der IDs der Prüfungen.
     * @return Eine Liste der gefundenen Prüfungen.
     */
    public List<Pruefung> getPruefungen(List<String> ids) {
        return pruefungRepository.findAllById(ids.stream().map(Long::parseLong).toList());
    }

    /**
     * Speichert eine Prüfung und aktualisiert die Änderungen.
     * 
     * @param pruefung Die Prüfung, die gespeichert werden soll.
     * @return Die gespeicherte und aktualisierte Prüfung.
     */
    public Pruefung saveAndFlush(Pruefung pruefung) {
        return pruefungRepository.saveAndFlush(pruefung);
    }

    /**
     * Fügt Testdaten für eine Prüfung ein.
     * 
     * @param pruefungsordnung Die Prüfungsordnung, zu der die Prüfung gehört.
     * @param modul            Das Modul, zu dem die Prüfung gehört.
     * @return Die erstellte Prüfung mit Testdaten.
     */
    public Pruefung insertTestData(Pruefungsordnung pruefungsordnung, Modul modul) {
        Pruefung pruefung = new Pruefung();
        pruefung.setPruefungsnummer(1);
        pruefung.setFachsemester(1);
        pruefung.setPruefungsordnung(pruefungsordnung);
        pruefung.setModul(modul);
        /*
         * PruefungKey key = new PruefungKey();
         * key.setModulId(modul.getId());
         * key.setPruefungsordnungId(pruefungsordnung.getId());
         */

        pruefung.setId(1l);

        pruefungRepository.saveAndFlush(pruefung);

        return pruefung;
    }

    /**
     * Diese Methode gibt eine Liste von Prüfungen zurück, die durch ihre IDs
     * identifiziert werden.
     *
     * @param list Eine Liste von Prüfungs-IDs, für die Prüfungen abgerufen werden
     *             sollen.
     * @return Eine Liste von Prüfungen, die den angegebenen IDs entsprechen.
     */
    public List<Pruefung> getPruefungenByIds(List<Long> list) {
        return pruefungRepository.findAllById(list);
    }
}