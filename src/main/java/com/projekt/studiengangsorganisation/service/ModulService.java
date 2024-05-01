package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.repository.ModulRepository;

@Service
public class ModulService {
    @Autowired
    ModulRepository modulRepository;

    public ModulService() {

    }

    public List<Modul> getModul() {
        return modulRepository.findAll();
    }

    public Optional<Modul> getModul(String id) {
        return modulRepository.findById(Long.parseLong(id));
    }

    public void insertTestData() {
        Modul modul = new Modul();
        modul.setName("Programmieren");
        modul.setKuerzel("5 WIF 22");
        modul.setWorkload(180);
        modul.setAbschluss("B. Sc.");
        modul.setArt("Seminaristischer Unterricht");
        modul.setBeschreibung("Programmieren lernen");
        modul.setCredits(6);
        modul.setDauer(1);
        modul.setFreigegeben(true);
        modul.setLehrveranstaltungsort("D2");

        modulRepository.save(modul);

    }
}
