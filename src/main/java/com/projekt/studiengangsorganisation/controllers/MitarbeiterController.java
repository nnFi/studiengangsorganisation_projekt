package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.MitarbeiterRepository;
import com.projekt.studiengangsorganisation.repository.NutzerRepository;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/mitarbeiter")
@RestController
public class MitarbeiterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    NutzerRepository nutzerRepository;

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    // Methode zum Abrufen eines Mitarbeiters anhand seiner ID
    @GetMapping("/{id}")
    public Mitarbeiter getOne(@PathVariable String id) {
        // Versuche, den Mitarbeiter mit der angegebenen ID zu erhalten
        Optional<Mitarbeiter> mitarbeiter = mitarbeiterService.getMitarbeiter(id);

        // Wenn der Mitarbeiter gefunden wurde, gib ihn zurück
        if (mitarbeiter.isPresent()) {
            return mitarbeiter.get();
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // Methode zum Abrufen aller Mitarbeiter
    @GetMapping("")
    public List<Mitarbeiter> getAll(HttpServletResponse response) {
        // Erhalte eine Liste aller Mitarbeiter
        List<Mitarbeiter> list = mitarbeiterService.getMitarbeiter();

        // Setze den Content-Range Header im Response, um die Anzahl der Mitarbeiter anzugeben
        response.setHeader("Content-Range", "1-" + list.size());

        // Gib die Liste der Mitarbeiter zurück
        return list;
    }

    // Methode zum Erstellen eines neuen Mitarbeiters
    @PostMapping("")
    public ResponseEntity<Mitarbeiter> createMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        // Überprüfe, ob der Benutzer ein Administrator ist, um einen Mitarbeiter zu erstellen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Nutzer> nutzer = nutzerRepository.findByUsername(authentication.getName());

        // Wenn der Benutzer ein Administrator ist
        if (nutzer.isPresent() && nutzer.get().getRole().equals("ADMIN")) {
            // Verschlüssele das Passwort des Mitarbeiters
            mitarbeiter.setPassword(passwordEncoder.encode(mitarbeiter.getPassword()));
            // Setze den Benutzernamen des Mitarbeiters basierend auf Vorname und Nachname
            mitarbeiter.setUsername(mitarbeiter.getVorname().toLowerCase() + "." + mitarbeiter.getNachname().toLowerCase());

            // Speichere den neuen Mitarbeiter in der Datenbank
            mitarbeiterService.saveAndFlush(mitarbeiter);

            // Gib eine Erfolgsantwort zurück
            return new ResponseEntity<>(mitarbeiter, HttpStatus.CREATED);
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mitarbeiter> updateMitarbeiter(@PathVariable String id, @RequestBody Mitarbeiter updatedMitarbeiter) {
        Optional<Mitarbeiter> existingMitarbeiter = mitarbeiterService.getMitarbeiter(id);

        if (existingMitarbeiter.isPresent()) {
            Mitarbeiter mitarbeiter = existingMitarbeiter.get();

            // Überprüfe, ob der Benutzer ein ADMIN ist
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Nutzer> nutzer = nutzerRepository.findByUsername(authentication.getName());
            if (!nutzer.isPresent() || !nutzer.get().getRole().equals("ADMIN")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nur Administratoren können Mitarbeiter aktualisieren");
            }

            // Aktualisiere die Felder des Mitarbeiters
            mitarbeiter.setVorname(updatedMitarbeiter.getVorname());
            mitarbeiter.setNachname(updatedMitarbeiter.getNachname());
            mitarbeiter.setUsername(updatedMitarbeiter.getVorname().toLowerCase() + "." + updatedMitarbeiter.getNachname().toLowerCase());

            // Speichere die aktualisierten Mitarbeiterdaten
            mitarbeiterService.saveAndFlush(mitarbeiter);

            return new ResponseEntity<>(mitarbeiter, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");
        }
    }
}