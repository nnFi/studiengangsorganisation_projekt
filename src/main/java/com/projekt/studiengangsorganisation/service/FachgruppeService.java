package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.repository.FachgruppeRepository;

@Service
public class FachgruppeService {

    @Autowired
    FachgruppeRepository fachgruppeRepository;

    public FachgruppeService() {

    }

    public List<Fachgruppe> getFachgruppen() {
        return fachgruppeRepository.findAll();
    }

    public Optional<Fachgruppe> getFachgruppe(String id) {
        return fachgruppeRepository.findById(Long.parseLong(id));
    }

    public Fachgruppe saveAndFlush(Fachgruppe fachgruppe) {
        return fachgruppeRepository.saveAndFlush(fachgruppe);
    }

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