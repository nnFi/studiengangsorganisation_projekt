package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
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

    public Optional<Fachbereich> getFachbereich(String id) {
        return fachbereichRepository.findById(Long.parseLong(id));
    }
}
