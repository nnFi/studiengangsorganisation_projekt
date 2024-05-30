package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.repository.FachbereichRepository;

@Service
public class FachbereichService {
    @Autowired
    FachbereichRepository fachbereichRepository;

    public FachbereichService() {

    }

    public List<Fachbereich> getFachbereiche() {
        return fachbereichRepository.findAll();
    }

    public Optional<Fachbereich> getFachbereich(Long id) {
        return fachbereichRepository.findById(id);
    }

    public Fachbereich saveAndFlush(Fachbereich fachbereich) {
        return fachbereichRepository.saveAndFlush(fachbereich);
    }

    public Fachbereich insertTestData(Mitarbeiter referent, Mitarbeiter stellvertreter) {
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName("Wirtschaft");
        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);

        fachbereichRepository.saveAndFlush(fachbereich);

        return fachbereich;
    }
}
