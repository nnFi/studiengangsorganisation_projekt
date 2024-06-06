package com.projekt.studiengangsorganisation.controllers;

import java.util.ArrayList;
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
import com.projekt.studiengangsorganisation.security.PasswordValidator;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/*
 * Controller-Klasse für die Verwaltung von Mitarbeitern.
 */
@RequestMapping("/mitarbeiter")
@RestController
public class MitarbeiterController {

    // Deklarierung Services
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    NutzerService nutzerService;

    /**
     * Methode zum Abrufen eines Mitarbeiters anhand seiner ID.
     * @param id Die ID des Mitarbeiters.
     * @return Der Mitarbeiter, falls gefunden.
     * @throws ResponseStatusException Falls der Mitarbeiter nicht gefunden wird.
     */
    @GetMapping("/{id}")
    public Mitarbeiter getOne(@PathVariable String id) {
        // Versuche, den Mitarbeiter mit der angegebenen ID zu erhalten
        Optional<Mitarbeiter> mitarbeiter = mitarbeiterService.getMitarbeiter(Long.parseLong(id));

        // Wenn der Mitarbeiter gefunden wurde, gib ihn zurück
        if (mitarbeiter.isPresent()) {
            return mitarbeiter.get();
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Mitarbeiter.
     * @param response Der HTTP-Response.
     * @return Eine Liste aller Mitarbeiter.
     */
    @GetMapping("")
    public List<Mitarbeiter> getAll(HttpServletResponse response) {
        // Erhalte eine Liste aller Mitarbeiter
        List<Mitarbeiter> list = mitarbeiterService.getMitarbeiter();

        // Setze den Content-Range Header im Response, um die Anzahl der Mitarbeiter anzugeben
        response.setHeader("Content-Range", "1-" + list.size());

        // Gib die Liste der Mitarbeiter zurück
        return list;
    }

    /**
     * Methode zum Erstellen eines neuen Mitarbeiters.
     * @param mitarbeiter Der Mitarbeiter, der erstellt werden soll.
     * @return Eine ResponseEntity, die den erstellten Mitarbeiter und den HTTP-Statuscode enthält.
     * @throws ResponseStatusException Falls der Benutzer kein Administrator ist.
     */
    @PostMapping("")
    public ResponseEntity<Mitarbeiter> createMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        // Überprüfe, ob der Benutzer ein Administrator ist, um einen Mitarbeiter zu erstellen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());

        // Wenn der Benutzer ein Administrator ist
        if (nutzer.isPresent() && nutzer.get().getRole().equals("ADMIN")) {
            // Verschlüssele das Passwort des Mitarbeiters
            mitarbeiter.setPassword(passwordEncoder.encode(mitarbeiter.getPassword()));
            // Setze den Benutzernamen des Mitarbeiters basierend auf Vorname und Nachname
            mitarbeiter.setUsername(mitarbeiter.getVorname().toLowerCase() + "." + mitarbeiter.getNachname().toLowerCase());

            // Validierungslogik für die Eingabefelder
            List<String> errors = validateMitarbeiter(mitarbeiter);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            // Speichere den neuen Mitarbeiter in der Datenbank
            mitarbeiterService.saveAndFlush(mitarbeiter);

            // Gib eine Erfolgsantwort zurück
            return new ResponseEntity<>(mitarbeiter, HttpStatus.CREATED);
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Aktualisieren eines Mitarbeiters.
     * @param id                Die ID des zu aktualisierenden Mitarbeiters.
     * @param updatedMitarbeiter Der aktualisierte Mitarbeiter.
     * @return Eine ResponseEntity, die den aktualisierten Mitarbeiter und den HTTP-Statuscode enthält.
     * @throws ResponseStatusException Falls der Mitarbeiter nicht gefunden wird oder der Benutzer kein Administrator ist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mitarbeiter> updateMitarbeiter(@PathVariable String id,
                                                         @RequestBody Mitarbeiter updatedMitarbeiter) {
        // Versuche, den vorhandenen Mitarbeiter anhand seiner ID zu erhalten
        Optional<Mitarbeiter> existingMitarbeiter = mitarbeiterService.getMitarbeiter(Long.parseLong(id));

        // Wenn der Mitarbeiter gefunden wurde
        if (existingMitarbeiter.isPresent()) {
            Mitarbeiter mitarbeiter = existingMitarbeiter.get();

            // Überprüfe, ob der Benutzer ein Administrator ist
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());
            if (!nutzer.isPresent() || !nutzer.get().getRole().equals("ADMIN")) {
                // Wenn der Benutzer kein Administrator ist, wirf einen Fehler 403 - Verboten
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Nur Administratoren können Mitarbeiter aktualisieren");
            }

            // Validierungslogik für die Eingabefelder
            List<String> errors = validateMitarbeiter(updatedMitarbeiter);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            // Aktualisiere die Felder des Mitarbeiters
            mitarbeiter.setVorname(updatedMitarbeiter.getVorname());
            mitarbeiter.setNachname(updatedMitarbeiter.getNachname());
            mitarbeiter.setUsername(
                    (updatedMitarbeiter.getVorname().toLowerCase() + "." + updatedMitarbeiter.getNachname().toLowerCase()).replace("ß", "ss"));

            // Speichere die aktualisierten Mitarbeiterdaten
            mitarbeiterService.saveAndFlush(mitarbeiter);

            // Gib eine Erfolgsantwort zurück
            return new ResponseEntity<>(mitarbeiter, HttpStatus.OK);
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");
        }
    }

    /**
     * Validiert das übergebene Mitarbeiter-Objekt.
     * 
     * @param mitarbeiter das zu validierende Mitarbeiter-Objekt
     * @return eine Liste von Fehlermeldungen, leer wenn keine Validierungsfehler vorliegen
     */
    private List<String> validateMitarbeiter(Mitarbeiter mitarbeiter) {
        List<String> errors = new ArrayList<>();

        // Überprüfung erforderlicher Felder
        if (mitarbeiter.getVorname() == null || mitarbeiter.getVorname().isEmpty()) {
            errors.add("Das Feld 'Vorname' ist erforderlich.");
        }

        if (mitarbeiter.getNachname() == null || mitarbeiter.getNachname().isEmpty()) {
            errors.add("Das Feld 'Nachname' ist erforderlich.");
        }

        // Längenprüfung
        if (mitarbeiter.getVorname() != null && mitarbeiter.getVorname().length() < 2) {
            errors.add("Das Feld 'Vorname' muss mindestens 2 Zeichen lang sein.");
        }

        if (mitarbeiter.getNachname() != null && mitarbeiter.getNachname().length() < 2) {
            errors.add("Das Feld 'Nachname' muss mindestens 2 Zeichen lang sein.");
        }
        
        // Benutzername-Formatprüfung
        String username = (mitarbeiter.getVorname().toLowerCase() + "." + mitarbeiter.getNachname().toLowerCase()).replace("ß", "ss");
        if (!username.matches("^[a-z0-9]+\\.[a-z0-9]+$")) {
            errors.add("Das Feld 'Username' hat ein ungültiges Format.");
        }

        // Passwort prüfen
        if (PasswordValidator.validate(mitarbeiter.getPassword())) {
            errors.add("Passwort entspricht nicht den Anforderungen. (Groß- und Kleinbuchstaben, Sonderzeichen, Zahlen, Mindeslänge 8)");
        }

        return errors;
    }
}