package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachgruppe;
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

}