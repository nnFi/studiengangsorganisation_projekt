package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.repository.StudiengangRepository;

@Service
public class StudiengangService {

    @Autowired
    StudiengangRepository studiengangRepository;

    public StudiengangService() {

    }

    public List<Studiengang> getStudiengaenge() {
        return studiengangRepository.findAll();
    }

    public Optional<Studiengang> getStudiengang(String id) {
        return studiengangRepository.findById(Long.parseLong(id));
    }

    public Studiengang insertTestData(Mitarbeiter leiter, Mitarbeiter stellvertreter, Fachbereich fachbereich) {
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss("Bachelor");
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);

        studiengangRepository.saveAndFlush(studiengang);

        return studiengang;
    }
}
