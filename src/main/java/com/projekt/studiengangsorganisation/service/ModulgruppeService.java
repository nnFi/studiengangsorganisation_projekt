package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.repository.ModulgruppeRepository;

@Service
public class ModulgruppeService {
    @Autowired
    ModulgruppeRepository modulgruppeRepository;

    public ModulgruppeService() {

    }

    public List<Modulgruppe> getModulgruppe() {
        return modulgruppeRepository.findAll();
    }

    public Optional<Modulgruppe> getModulgruppe(String id) {
        return modulgruppeRepository.findById(Long.parseLong(id));
    }

    public void insertTestData() {
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("IBM");

        modulgruppeRepository.save(modulgruppe);
    }
}
