package com.projekt.studiengangsorganisation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;
import com.projekt.studiengangsorganisation.service.StudentService;
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

    @Autowired
    StudentService studentService;

    @Autowired
    ModulgruppeService modulgruppeService;

    @Autowired
    ModulService modulService;

    @Autowired
    PruefungService pruefungService;

    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @PostMapping("")
    public String createTestData() {

        Mitarbeiter mitarbeiter1 = mitarbeiterService.insertTestData();
        Mitarbeiter mitarbeiter2 = mitarbeiterService.insertTestData();

        Student student = studentService.insertTestData();

        Fachbereich fachbereich = fachbereichService.insertTestData(mitarbeiter1, mitarbeiter2);

        Fachgruppe fachgruppe = fachgruppeService.insertTestData(mitarbeiter1, mitarbeiter2, fachbereich);

        Modulgruppe modulgruppe = modulgruppeService.insertTestData();

        Modul modul = modulService.insertTestData(fachgruppe, mitarbeiter1, null, modulgruppe);

        Studiengang studiengang = studiengangService.insertTestData(mitarbeiter1, mitarbeiter2, fachbereich);

        Pruefungsordnung pruefungsordnung = pruefungsordnungService.insertTestData(studiengang);

        Pruefung pruefung = pruefungService.insertTestData(pruefungsordnung, modul);

        return "Testdaten wurden erfolgreich erstellt!";
    }

}
