package com.projekt.studiengangsorganisation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.repository.PruefungRepository;

@Service
public class PruefungService {

    @Autowired
    PruefungRepository pruefungRepository;

    public PruefungService() {}

    public List<Pruefung> getPruefungen() {
        return pruefungRepository.findAll();
    }

    public void insertTestData() {
        Pruefung pruefung = new Pruefung();
        pruefung.setPruefungsnummer(1);

        pruefungRepository.save(pruefung);
    }
}
