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

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.StudiengangService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/studiengang")
@RestController
public class StudiengangController {

    @Autowired
    StudiengangService studiengangService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    FachbereichService fachbereichService;

    @Autowired
    NutzerService nutzerService;

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
    public List<Studiengang> getAll(HttpServletResponse response) {
        List<Studiengang> list = studiengangService.getStudiengaenge();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Studiengang> createFachbereich(@RequestBody Studiengang studiengang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Mitarbeiter leiter = mitarbeiterService
                .getMitarbeiter(studiengang.getLeiterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leiter not found"));

        Mitarbeiter stellvertretenderLeiter = mitarbeiterService
                .getMitarbeiter(studiengang.getStellvertreterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter not found"));

        Fachbereich fachbereich = fachbereichService
                .getFachbereich(studiengang.getFachbereichId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich not found"));

        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertretenderLeiter);
        studiengang.setFachbereich(fachbereich);
        studiengangService.saveAndFlush(studiengang);

        return new ResponseEntity<>(studiengang, HttpStatus.CREATED);
    }
}
