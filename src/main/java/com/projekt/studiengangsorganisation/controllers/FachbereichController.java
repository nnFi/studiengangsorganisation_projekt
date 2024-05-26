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
import com.projekt.studiengangsorganisation.repository.FachbereichRepository;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/fachbereich")
@RestController
public class FachbereichController {

    @Autowired
    FachbereichService fachbereichService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    FachbereichRepository fachbereichRepository;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @GetMapping("/{id}")
    public Fachbereich getOne(@PathVariable String id) {
        Optional<Fachbereich> fachbereich = fachbereichService.getFachbereich(id);

        if (fachbereich.isPresent()) {
            return fachbereich.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Fachbereich> getAll(HttpServletResponse response) {
        List<Fachbereich> list = fachbereichService.getFachbereiche();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Fachbereich> createFachbereich(@RequestBody Fachbereich fachbereich) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Mitarbeiter referent = mitarbeiterService
                .getMitarbeiter(fachbereich.getReferentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent not found"));

        Mitarbeiter stellvertreter = mitarbeiterService
                .getMitarbeiter(fachbereich.getStellvertreterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter not found"));

        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);
        fachbereichRepository.saveAndFlush(fachbereich);

        return new ResponseEntity<>(fachbereich, HttpStatus.CREATED);
    }
}