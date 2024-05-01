package com.projekt.studiengangsorganisation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.StudiengangService;

@RequestMapping("/testdata")
@RestController
public class TestDataController {

    @Autowired
    StudiengangService studiengangService;

    @Autowired
    FachgruppeService fachgruppeService;

    @Autowired
    FachbereichService fachbereichService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @PostMapping("")
    public String createTestData() {
        studiengangService.insertTestData();
        Mitarbeiter mitarbeiter1 = mitarbeiterService.insertTestData();
        Mitarbeiter mitarbeiter2 = mitarbeiterService.insertTestData();
        Fachbereich fachbereich = fachbereichService.insertTestData(mitarbeiter1, mitarbeiter2);
        fachgruppeService.insertTestData(mitarbeiter1, mitarbeiter2, fachbereich);

        return "Testdaten wurden erfolgreich erstellt!";
    }

}
