package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.NutzerRepository;

@Service
public class NutzerService {
    
    @Autowired
    NutzerRepository nutzerRepository;

    public NutzerService() {

    }

    public List<Nutzer> getNutzer() {
    return nutzerRepository.findAll();
    }


    public Optional<Nutzer> getNutzer(String id) {
        return nutzerRepository.findById(Long.parseLong(id));
    }  
}
