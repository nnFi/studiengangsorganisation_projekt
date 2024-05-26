package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.repository.PruefungRepository;

@Service
public class PruefungService {

    @Autowired
    PruefungRepository pruefungRepository;

    public PruefungService() {
    }

    public Optional<Pruefung> getPruefung(String id) {
        return pruefungRepository.findById(Long.parseLong(id));
    }

    public List<Pruefung> getPruefungen() {
        return pruefungRepository.findAll();
    }

    public List<Pruefung> getPruefungen(List<String> ids) {
        return pruefungRepository.findAllById(ids.stream().map(Long::parseLong).toList());
    }

    public Pruefung saveAndFlush(Pruefung pruefung) {
        return pruefungRepository.saveAndFlush(pruefung);
    }

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
}
