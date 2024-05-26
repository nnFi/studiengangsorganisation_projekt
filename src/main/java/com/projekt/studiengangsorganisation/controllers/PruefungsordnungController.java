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

import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;
import com.projekt.studiengangsorganisation.service.StudiengangService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/pruefungsordnung")
@RestController
public class PruefungsordnungController {

    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    PruefungService pruefungService;

    @Autowired
    StudiengangService studiengangService;

    @GetMapping("/{id}")
    public Pruefungsordnung getOne(@PathVariable String id) {
        Optional<Pruefungsordnung> pruefungsordnung = pruefungsordnungService.getPruefungsordnung(id);

        if (pruefungsordnung.isPresent()) {
            return pruefungsordnung.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Pruefungsordnung> getAll(HttpServletResponse response) {
        List<Pruefungsordnung> list = pruefungsordnungService.getPruefungsordnungen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Pruefungsordnung> createFachbereich(@RequestBody Pruefungsordnung pruefungsordnung) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Studiengang studiengang = studiengangService
                .getStudiengang(pruefungsordnung.getStudiengangId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leiter not found"));

        pruefungsordnung.setStudiengang(studiengang);
        pruefungsordnungService.saveAndFlush(pruefungsordnung);

        return new ResponseEntity<>(pruefungsordnung, HttpStatus.CREATED);
    }
}
