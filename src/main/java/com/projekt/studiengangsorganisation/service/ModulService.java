package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.repository.ModulRepository;

@Service
public class ModulService {
    @Autowired
    ModulRepository modulRepository;

    public ModulService() {

    }

    public List<Modul> getModule() {
        return modulRepository.findAll();
    }

    public Optional<Modul> getModul(String id) {
        return modulRepository.findById(Long.parseLong(id));
    }

    public Modul saveAndFlush(Modul modul) {
        return modulRepository.saveAndFlush(modul);
    }

    public Modul insertTestData(Fachgruppe fachgruppe, Mitarbeiter beauftragter, Set<Pruefung> pruefungen,
            Modulgruppe modulgruppe) {
        Modul modul = new Modul();
        modul.setName("Programmieren");
        modul.setModulnummer("5 WIF 22");
        modul.setWorkload(180);
        modul.setAbschluss("B. Sc.");
        modul.setArt("Seminaristischer Unterricht");
        modul.setBeschreibung("Programmieren lernen");
        modul.setCredits(6);
        modul.setDauer(1);
        modul.setFreigegeben(true);
        modul.setLehrveranstaltungsort("D2");

        modul.setFachgruppe(fachgruppe);
        modul.setModulbeauftragter(beauftragter);
        modul.setPruefungen(pruefungen);
        modul.setModulgruppe(modulgruppe);

        modulRepository.saveAndFlush(modul);

        return modul;
    }
}
