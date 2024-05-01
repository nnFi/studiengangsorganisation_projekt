package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.repository.PruefungsordnungRepository;

@Service
public class PruefungsordnungService {
    @Autowired
    PruefungsordnungRepository pruefungsordnungRepository;

    public PruefungsordnungService() {}

    public List<Pruefungsordnung> getFachgruppen() {
        return pruefungsordnungRepository.findAll();
    }

    public Optional<Pruefungsordnung> getFachgruppe(String id) {
        return pruefungsordnungRepository.findById(Long.parseLong(id));
    }

    public void insertTestData() {
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setFreigegeben(false);
        pruefungsordnung.setVersion("1");

        pruefungsordnungRepository.save(pruefungsordnung);
    }
}
