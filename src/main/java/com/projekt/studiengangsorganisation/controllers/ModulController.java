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

import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/modul")
@RestController
public class ModulController {

    @Autowired
    ModulService modulService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    FachgruppeService fachgruppeService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    ModulgruppeService modulgruppeService;

    @GetMapping("/{id}")
    public Modul getOne(@PathVariable String id) {
        Optional<Modul> modul = modulService.getModul(id);

        if (modul.isPresent()) {
            return modul.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Modul> getAll(HttpServletResponse response) {
        List<Modul> list = modulService.getModule();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Modul> createModul(@RequestBody Modul modul) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Fachgruppe fachgruppe = fachgruppeService
                .getFachgruppe(modul.getFachgruppeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachgruppe not found"));

        if (!fachgruppe.getReferent().getId().equals(nutzer.getId())
                && !fachgruppe.getStellvertreter().getId().equals(nutzer.getId())
                && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        Mitarbeiter modulbeauftragter = mitarbeiterService
                .getMitarbeiter(modul.getModulbeauftragerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich not found"));

        Modulgruppe modulgruppe = modulgruppeService
                .getModulgruppe(modul.getModulgruppeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich not found"));

        modul.setFachgruppe(fachgruppe);
        modul.setModulbeauftragter(modulbeauftragter);
        modul.setModulgruppe(modulgruppe);
        modulService.saveAndFlush(modul);

        return new ResponseEntity<>(modul, HttpStatus.CREATED);
    }
}
