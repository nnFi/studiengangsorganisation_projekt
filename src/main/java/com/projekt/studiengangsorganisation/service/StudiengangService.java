package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Abschluss;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.repository.StudiengangRepository;

/**
 * Service-Klasse für die Verwaltung von Studiengängen.
 */
@Service
public class StudiengangService {

    @Autowired
    StudiengangRepository studiengangRepository;

    /**
     * Konstruktor für die StudiengangService-Klasse.
     */
    public StudiengangService() {

    }

    /**
     * Holt alle Studiengänge.
     * 
     * @return Eine Liste aller vorhandenen Studiengänge.
     */
    public List<Studiengang> getStudiengaenge() {
        return studiengangRepository.findAll();
    }

    /**
     * Holt einen Studiengang anhand seiner ID.
     * 
     * @param id Die ID des Studiengangs.
     * @return Ein Optional, das den gefundenen Studiengang enthält, falls
     *         vorhanden.
     */
    public Optional<Studiengang> getStudiengang(Long id) {
        return studiengangRepository.findById(id);
    }

    /**
     * Holt einen Studiengang anhand von Name und Abschluss.
     * 
     * @param name      Der Name des Studiengangs.
     * @param abschluss Der Abschluss des Studiengangs.
     * @return Ein Optional, das den gefundenen Studiengang enthält, falls
     *         vorhanden.
     */
    public Optional<Studiengang> getStudiengang(String name, Abschluss abschluss) {
        return studiengangRepository.findByNameAndAbschluss(name, abschluss);
    }

    /**
     * Speichert einen Studiengang und aktualisiert die Änderungen.
     * 
     * @param studiengang Der Studiengang, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Studiengang.
     */
    public Studiengang saveAndFlush(Studiengang studiengang) {
        return studiengangRepository.saveAndFlush(studiengang);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        studiengangRepository.deleteAll();
    }

}