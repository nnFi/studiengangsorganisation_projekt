package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void insertTestData() {
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss("Bachelor");
        studiengang.setRegelstudienzeit(6);

        studiengangRepository.save(studiengang);
    }
}
