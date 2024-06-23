package com.projekt.studiengangsorganisation.controllers;

import java.util.ArrayList;
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
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für Fachbereich-Operationen.
 */
@RequestMapping("/fachbereich")
@RestController
public class FachbereichController {

    // Deklarierung Services
    @Autowired
    FachbereichService fachbereichService; 

    @Autowired
    NutzerService nutzerService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    /**
     * Methode zum Abrufen eines Fachbereich anhand seiner ID.
     * @param id Die ID des Fachbereichs.
     * @return Der Fachbereich, falls gefunden.
     * @throws ResponseStatusException Falls kein Fachbereich mit der angegebenen ID gefunden wurde (Status: NOT_FOUND).
     */
    @GetMapping("/{id}")
    public Fachbereich getOne(@PathVariable String id) {
        Optional<Fachbereich> fachbereich = fachbereichService.getFachbereich(Long.parseLong(id));

        if (fachbereich.isPresent()) {
            Fachbereich fachbereichObject = fachbereich.get();
            fachbereichObject.setReferentId(fachbereichObject.getReferent().getId());
            fachbereichObject.setStellvertreterId(fachbereichObject.getStellvertreter().getId());
            return fachbereichObject;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Fachbereiche.
     * @param response Das HTTP-Response-Objekt.
     * @return Die Liste aller Fachbereiche.
     */
    @GetMapping("")
    public List<Fachbereich> getAll(HttpServletResponse response) {
        List<Fachbereich> list = fachbereichService.getFachbereiche();

        list.forEach(fachbereich -> {
            fachbereich.setReferentId(fachbereich.getReferent().getId());
            fachbereich.setStellvertreterId(fachbereich.getStellvertreter().getId());
        });

        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    /**
     * Methode zum Erstellen eines neuen Fachbereich.
     * @param fachbereich Der zu erstellende Fachbereich.
     * @return Die HTTP-Response-Entität mit dem erstellten Fachbereich und dem Statuscode 201 (CREATED).
     */
    @PostMapping("")
    public ResponseEntity<Fachbereich> createFachbereich(@RequestBody Fachbereich fachbereich) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht authorisiert"));

        // Überprüfen, ob der Benutzer die erforderliche Rolle hat, um die Operation
        // auszuführen
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        Mitarbeiter referent = mitarbeiterService
                .getMitarbeiter(fachbereich.getReferentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent micht gefunden"));

        Mitarbeiter stellvertreter = mitarbeiterService
                .getMitarbeiter(fachbereich.getStellvertreterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter nicht gefunden"));

        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);

        // Validierungslogik für die Eingabefelder
        List<String> errors = validateFachbereich(fachbereich);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        fachbereichService.saveAndFlush(fachbereich);

        return new ResponseEntity<>(fachbereich, HttpStatus.CREATED);
    }

    /**
     * Methode zum Aktualisieren eines vorhandenen Fachbereich.
     * @param id Die ID des zu aktualisierenden Fachbereichs.
     * @param updatedFachbereich Der aktualisierte Fachbereich.
     * @return Die HTTP-Response-Entität mit dem aktualisierten Fachbereich und dem Statuscode 200 (OK).
     * @throws ResponseStatusException Falls der Fachbereich nicht gefunden wurde (Status: NOT_FOUND) oder
     *                                  der Benutzer keine Berechtigung hat (Status: FORBIDDEN).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fachbereich> updateFachbereich(@PathVariable Long id, @RequestBody Fachbereich updatedFachbereich) {
        Optional<Fachbereich> existingFachbereich = fachbereichService.getFachbereich(id);

        if (existingFachbereich.isPresent()) {
            Fachbereich fachbereich = existingFachbereich.get();

            // Authentifizierung des Benutzers über den SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Benutzerinformationen abrufen und sicherstellen, dass der Benutzer
            // autorisiert ist
            Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

            // Überprüft ob der Benutzer eine Prüfung bearbeiten darf und gibt im Fehlerfall 401 zurück
            if (!(nutzer.getRole().equals("ADMIN")
                || nutzer.getRole().equals("MITARBEITER")
                    && (fachbereich.getReferent().getId() == nutzer.getId()
                        || fachbereich.getStellvertreter().getId() == nutzer.getId()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
            }

            // Referent und Stellvertreter finden
            Mitarbeiter referent = mitarbeiterService
                    .getMitarbeiter(updatedFachbereich.getReferentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent nicht gefunden"));

            Mitarbeiter stellvertreter = mitarbeiterService
                    .getMitarbeiter(updatedFachbereich.getStellvertreterId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter nicht gefunden"));

            // Fachbereich aktualisieren
            fachbereich.setReferent(referent);
            fachbereich.setStellvertreter(stellvertreter);

            // Validierungslogik für die Eingabefelder
            List<String> errors = validateFachbereich(fachbereich);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            fachbereichService.saveAndFlush(fachbereich);

            return new ResponseEntity<>(fachbereich, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich nicht gefunden");
        }
    }

    /**
     * Validiert das übergebene Fachbereich-Objekt.
     * 
     * @param fachbereich das zu validierende Fachbereich-Objekt
     * @return eine Liste von Fehlermeldungen, leer wenn keine Validierungsfehler vorliegen
     */
    List<String> validateFachbereich(Fachbereich fachbereich) {
        List<String> errors = new ArrayList<>();

        // Namensprüfung
        if (fachbereich.getName() == null || fachbereich.getName().isEmpty()) {
            errors.add("Das Feld 'Nname' ist erforderlich.");
        }

        // Längenprüfung
        if (fachbereich.getName() != null && fachbereich.getName().length() < 2) {
            errors.add("Das Feld 'Name' muss mindestens 2 Zeichen lang sein.");
        }

        // Überprüfen, ob das Feld ReferentId gesetzt ist
        if (fachbereich.getReferentId() == null) {
            errors.add("ReferentId ist nicht gesetzt.");
        }

        // Überprüfen, ob das Feld StellvertreterId gesetzt ist
        if (fachbereich.getStellvertreterId() == null) {
            errors.add("StellvertreterId ist nicht gesetzt.");
        }

        return errors;
    }
}