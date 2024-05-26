package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/pruefung")
@RestController
public class PruefungController {

    @Autowired
    PruefungService pruefungService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @Autowired
    ModulService modulService;

    @GetMapping("/{id}")
    public Pruefung getOne(@PathVariable String id) {
        Optional<Pruefung> pruefung = pruefungService.getPruefung(id);

        if (pruefung.isPresent()) {
            return pruefung.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Pruefung> getAll(HttpServletResponse response) {
        List<Pruefung> list = pruefungService.getPruefungen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Pruefung> createPruefung(@RequestBody Pruefung pruefung) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Pruefungsordnung pruefungsordnung = pruefungsordnungService
                .getPruefungsordnung((pruefung.getPruefungsordnungId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pruefungsordnung not found"));

        for (Pruefung p : pruefungsordnung.getPruefungen()) {
            if (p.getModulId().equals(pruefung.getModulId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Pruefung already exists");
            }
        }

        Modul modul = modulService
                .getModul((pruefung.getModulId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul not found"));

        pruefung.setPruefungsordnung(pruefungsordnung);
        pruefung.setModul(modul);

        pruefungService.saveAndFlush(pruefung);

        return new ResponseEntity<>(pruefung, HttpStatus.CREATED);
    }
}
