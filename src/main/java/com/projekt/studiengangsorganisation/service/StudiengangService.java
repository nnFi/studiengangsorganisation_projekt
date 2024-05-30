package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Abschluss;
import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
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
     * @return Eine Liste aller vorhandenen Studiengänge.
     */
    public List<Studiengang> getStudiengaenge() {
        return studiengangRepository.findAll();
    }

    /**
     * Holt einen Studiengang anhand seiner ID.
     * @param id Die ID des Studiengangs.
     * @return Ein Optional, das den gefundenen Studiengang enthält, falls vorhanden.
     */
    public Optional<Studiengang> getStudiengang(Long id) {
        return studiengangRepository.findById(id);
    }

    /**
     * Speichert einen Studiengang und aktualisiert die Änderungen.
     * @param studiengang Der Studiengang, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Studiengang.
     */
    public Studiengang saveAndFlush(Studiengang studiengang) {
        return studiengangRepository.saveAndFlush(studiengang);
    }

    /**
     * Fügt Testdaten für einen Studiengang ein.
     * @param leiter Der Leiter des Studiengangs.
     * @param stellvertreter Der stellvertretende Leiter des Studiengangs.
     * @param fachbereich Der Fachbereich, zu dem der Studiengang gehört.
     * @return Der erstellte Studiengang mit Testdaten.
     */
    public Studiengang insertTestData(Mitarbeiter leiter, Mitarbeiter stellvertreter, Fachbereich fachbereich) {
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);

        studiengangRepository.saveAndFlush(studiengang);

        return studiengang;
    }
}