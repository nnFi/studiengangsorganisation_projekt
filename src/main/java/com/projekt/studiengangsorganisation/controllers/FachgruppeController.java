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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.FachgruppeRepository;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/fachgruppe")
@RestController
public class FachgruppeController {

    @Autowired
    NutzerService nutzerService;

    @Autowired
    FachgruppeService fachgruppeService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    FachbereichService fachbereichService;

    @Autowired
    FachgruppeRepository fachgruppeRepository;

    @GetMapping("/{id}")
    public Fachgruppe getOne(@PathVariable String id) {
        Optional<Fachgruppe> fachgruppe = fachgruppeService.getFachgruppe(Long.parseLong(id));

        if (fachgruppe.isPresent()) {
            Fachgruppe fachgruppeObject = fachgruppe.get();
            fachgruppeObject.setFachbereichId(fachgruppeObject.getFachbereich().getId());
            fachgruppeObject.setReferentId(fachgruppeObject.getReferent().getId());
            fachgruppeObject.setStellvertreterId(fachgruppeObject.getStellvertreter().getId());

            return fachgruppeObject;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Fachgruppe> getAll(HttpServletResponse response) {
        List<Fachgruppe> list = fachgruppeService.getFachgruppen();

        list.forEach(fachgruppe -> {
            fachgruppe.setFachbereichId(fachgruppe.getFachbereich().getId());
            fachgruppe.setReferentId(fachgruppe.getReferent().getId());
            fachgruppe.setStellvertreterId(fachgruppe.getStellvertreter().getId());
        });

        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Fachgruppe> createFachgruppe(@RequestBody Fachgruppe fachgruppe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Fachbereich fachbereich = fachbereichService
                .getFachbereich(fachgruppe.getFachbereichId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich not found"));

        if (!fachbereich.getReferent().getId().equals(nutzer.getId())
                && !fachbereich.getStellvertreter().getId().equals(nutzer.getId())
                && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Mitarbeiter referent = mitarbeiterService
                .getMitarbeiter(fachgruppe.getReferentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent not found"));

        Mitarbeiter stellvertreter = mitarbeiterService
                .getMitarbeiter(fachgruppe.getStellvertreterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter not found"));

        fachgruppe.setFachbereich(fachbereich);
        fachgruppe.setReferent(referent);
        fachgruppe.setStellvertreter(stellvertreter);
        fachgruppeRepository.saveAndFlush(fachgruppe);

        return new ResponseEntity<>(fachgruppe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fachgruppe> updateFachgruppe(@PathVariable String id, @RequestBody Fachgruppe updatedFachgruppe) {
        Optional<Fachgruppe> existingFachgruppe = fachgruppeService.getFachgruppe(Long.parseLong(id));

        if (existingFachgruppe.isPresent()) {
            Fachgruppe fachgruppe = existingFachgruppe.get();

            // Überprüfe, ob der Benutzer ein ADMIN ist
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());
            if (!nutzer.isPresent() || !nutzer.get().getRole().equals("ADMIN")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Nur Administratoren können Modul aktualisieren");
            }

            Mitarbeiter referent = mitarbeiterService
                    .getMitarbeiter(updatedFachgruppe.getReferentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent not found"));

            Mitarbeiter stellvertreter= mitarbeiterService
                    .getMitarbeiter(updatedFachgruppe.getStellvertreterId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter not found"));

            Fachbereich fachbereich= fachbereichService
                    .getFachbereich(updatedFachgruppe.getFachbereichId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich not found"));

            fachgruppe.setId(updatedFachgruppe.getId());
            fachgruppe.setName(updatedFachgruppe.getName());
            fachgruppe.setKuerzel(updatedFachgruppe.getKuerzel());
            fachgruppe.setFachbereich(fachbereich);
            fachgruppe.setReferent(referent);
            fachgruppe.setStellvertreter(stellvertreter);
            fachgruppeService.saveAndFlush(fachgruppe);

            return new ResponseEntity<>(fachgruppe, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachgruppe nicht gefunden");
        }
    }
}
