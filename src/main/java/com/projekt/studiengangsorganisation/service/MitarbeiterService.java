package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.repository.MitarbeiterRepository;

@Service
public class MitarbeiterService {

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    public MitarbeiterService() {

    }

    public Optional<Mitarbeiter> getMitarbeiter(String id) {
        return mitarbeiterRepository.findById(Long.parseLong(id));
    }

    public List<Mitarbeiter> getMitarbeiter() {
        return mitarbeiterRepository.findAll();
    }

    public Mitarbeiter saveAndFlush(Mitarbeiter mitarbeiter) {
        return mitarbeiterRepository.saveAndFlush(mitarbeiter);
    }

    public Mitarbeiter insertTestData() {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname("Max");
        mitarbeiter.setNachname("Mustermann");

        mitarbeiterRepository.saveAndFlush(mitarbeiter);

        return mitarbeiter;
    }
}
