package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
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
     * @return Eine Liste aller vorhandenen Fachgruppen.
     */
    public List<Fachgruppe> getFachgruppen() {
        return fachgruppeRepository.findAll();
    }

    /**
     * Holt eine Fachgruppe anhand ihrer ID.
     * @param id Die ID der Fachgruppe.
     * @return Ein Optional, das die gefundene Fachgruppe enthält, falls vorhanden.
     */
    public Optional<Fachgruppe> getFachgruppe(Long id) {
        return fachgruppeRepository.findById(id);
    }

    /**
     * Speichert eine Fachgruppe und aktualisiert die Änderungen.
     * @param fachgruppe Die Fachgruppe, die gespeichert werden soll.
     * @return Die gespeicherte und aktualisierte Fachgruppe.
     */
    public Fachgruppe saveAndFlush(Fachgruppe fachgruppe) {
        return fachgruppeRepository.saveAndFlush(fachgruppe);
    }

    /**
     * Fügt Testdaten für eine Fachgruppe ein.
     * @param referent Der Referent der Fachgruppe.
     * @param stellvertreter Der Stellvertreter der Fachgruppe.
     * @param fachbereich Der Fachbereich, zu dem die Fachgruppe gehört.
     * @return Die erstellte Fachgruppe mit Testdaten.
     */
    public Fachgruppe insertTestData(Mitarbeiter referent, Mitarbeiter stellvertreter, Fachbereich fachbereich) {
        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setName("Marketing");
        fachgruppe.setFachbereich(fachbereich);
        fachgruppe.setReferent(referent);
        fachgruppe.setStellvertreter(stellvertreter);

        fachgruppeRepository.saveAndFlush(fachgruppe);

        return fachgruppe;
    }
}