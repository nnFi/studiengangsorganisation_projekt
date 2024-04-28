package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.StudiengangService;

@RequestMapping("/studiengang")
@RestController
public class StudiengangController {

    @Autowired
    StudiengangService studiengangService;

    @GetMapping("/{id}")
    public Studiengang getOne(@PathVariable String id) {
        Optional<Studiengang> studiengang = studiengangService.getStudiengang(id);

        if (studiengang.isPresent()) {
            return studiengang.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Studiengang> getMethodName() {
        return studiengangService.getStudiengaenge();
    }

    @PostMapping("/insertTest")
    public String createTestData() {
        studiengangService.insertTestData();

        return "Testdaten wurden erfolgreich erstellt!";
    }

}
