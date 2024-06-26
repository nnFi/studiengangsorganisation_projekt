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
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für die Verwaltung von Fachgruppen.
 */
@RequestMapping("/api/fachgruppe")
@RestController
public class FachgruppeController {

    // Deklarierung Services
    @Autowired
    NutzerService nutzerService;

    @Autowired
    FachgruppeService fachgruppeService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    FachbereichService fachbereichService;

    /**
     * Methode zum Abrufen einer Fachgruppe basierend auf der ID.
     * 
     * @param id Die ID der Fachgruppe.
     * @return Die gefundene Fachgruppe.
     * @throws ResponseStatusException Falls die Fachgruppe nicht gefunden wird.
     */
    @GetMapping("/{id}")
    public Fachgruppe getOne(@PathVariable String id) {
        Optional<Fachgruppe> fachgruppe = fachgruppeService.getFachgruppe(Long.parseLong(id));

        if (fachgruppe.isPresent()) {
            Fachgruppe fachgruppeObject = fachgruppe.get();
            fachgruppeObject.setFachbereichId(fachgruppeObject.getFachbereich().getId());
            fachgruppeObject.setReferentId(fachgruppeObject.getReferent().getId());
            fachgruppeObject.setStellvertreterId(fachgruppeObject.getStellvertreter().getId());
            return fachgruppeObject; // Falls vorhanden, gib die Fachgruppe zurück
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // Andernfalls: Nicht gefunden Fehler
        }
    }

    /**
     * Methode zum Abrufen aller Fachgruppen.
     * 
     * @param response Die HTTP-Response.
     * @return Eine Liste aller Fachgruppen.
     */
    @GetMapping("")
    public List<Fachgruppe> getAll(HttpServletResponse response) {
        List<Fachgruppe> list = fachgruppeService.getFachgruppen();

        list.forEach(fachgruppe -> {
            fachgruppe.setFachbereichId(fachgruppe.getFachbereich().getId());
            fachgruppe.setReferentId(fachgruppe.getReferent().getId());
            fachgruppe.setStellvertreterId(fachgruppe.getStellvertreter().getId());
        });

        response.setHeader("Content-Range", "1-" + list.size()); // Setze Content-Range Header
        return list; // Gib die Liste der Fachgruppen zurück
    }

    /**
     * Methode zum Erstellen einer neue Fachgruppe.
     * 
     * @param fachgruppe Die zu erstellende Fachgruppenentität.
     * @return Die erstellte Fachgruppenentität.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist oder
     *                                 erforderliche Ressourcen nicht gefunden
     *                                 werden.
     */
    @PostMapping("")
    public ResponseEntity<Fachgruppe> createFachgruppe(@RequestBody Fachgruppe fachgruppe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        // Überprüfen, ob der Benutzer die erforderliche Rolle hat, um die Operation
        // auszuführen
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        Fachbereich fachbereich = fachbereichService
                .getFachbereich(fachgruppe.getFachbereichId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich nicht gefunden"));

        if (!fachbereich.getReferent().getId().equals(nutzer.getId())
                && !fachbereich.getStellvertreter().getId().equals(nutzer.getId())
                && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        Mitarbeiter referent = mitarbeiterService
                .getMitarbeiter(fachgruppe.getReferentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent nicht gefunden "));

        Mitarbeiter stellvertreter = mitarbeiterService
                .getMitarbeiter(fachgruppe.getStellvertreterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter nicht gefunden"));

        // Überprüfen, ob es bereits eine Fachgruppe mit demselben Namen gibt
        if (fachgruppeService.getFachgruppeByName(fachgruppe.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fachgruppe mit diesem Namen existiert bereits");
        }

        // Überprüfen, ob es bereits eine Fachgruppe mit demselben Kürzel gibt
        if (fachgruppeService.getFachgruppeByKuerzel(fachgruppe.getKuerzel()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fachgruppe mit diesem Kürzel existiert bereits");
        }

        fachgruppe.setFachbereich(fachbereich);
        fachgruppe.setReferent(referent);
        fachgruppe.setStellvertreter(stellvertreter);

        // Validierungslogik für die Eingabefelder
        List<String> errors = validateFachgruppe(fachgruppe);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        fachgruppeService.saveAndFlush(fachgruppe); // Fachgruppe speichern

        return new ResponseEntity<>(fachgruppe, HttpStatus.CREATED); // Erfolgreiche Erstellung
    }

    /**
     * Methode zum Aktualisieren einer vorhandene Fachgruppe.
     * 
     * @param id                Die ID der zu aktualisierenden Fachgruppe.
     * @param updatedFachgruppe Die aktualisierte Fachgruppenentität.
     * @return Die aktualisierte Fachgruppenentität.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist, die
     *                                 Fachgruppe nicht gefunden wird oder die
     *                                 Aktualisierung fehlschlägt.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fachgruppe> updateFachgruppe(@PathVariable String id,
            @RequestBody Fachgruppe updatedFachgruppe) {

        Optional<Fachgruppe> existingFachgruppe = fachgruppeService.getFachgruppe(Long.parseLong(id));

        // Authentifizierung des Benutzers über den SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Benutzerinformationen abrufen und sicherstellen, dass der Benutzer
        // autorisiert ist
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        if (existingFachgruppe.isPresent()) {
            Fachgruppe fachgruppe = existingFachgruppe.get();

            // Überprüft ob der Benutzer eine Prüfung bearbeiten darf und gibt im Fehlerfall
            // 401 zurück
            if (!(nutzer.getRole().equals("ADMIN")
                    || nutzer.getRole().equals("MITARBEITER")
                            && (fachgruppe.getReferent().getId() == nutzer.getId()
                                    || fachgruppe.getStellvertreter().getId() == nutzer.getId()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
            }

            // Mitarbeiterreferent, Stellvertreter und Fachbereich anhand ihrer IDs abrufen.
            Mitarbeiter referent = mitarbeiterService.getMitarbeiter(updatedFachgruppe.getReferentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referent nicht gefunden"));

            Mitarbeiter stellvertreter = mitarbeiterService.getMitarbeiter(updatedFachgruppe.getStellvertreterId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter nicht gefunden"));

            Fachbereich fachbereich = fachbereichService.getFachbereich(updatedFachgruppe.getFachbereichId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich nicht gefunden"));

            // Die Eigenschaften der aktualisierten Fachgruppe setzen.
            fachgruppe.setId(updatedFachgruppe.getId());
            fachgruppe.setName(updatedFachgruppe.getName());
            fachgruppe.setKuerzel(updatedFachgruppe.getKuerzel());
            fachgruppe.setFachbereich(fachbereich);
            fachgruppe.setReferent(referent);
            fachgruppe.setStellvertreter(stellvertreter);

            // Validierungslogik für die Eingabefelder
            List<String> errors = validateFachgruppe(fachgruppe);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            // Die aktualisierte Fachgruppe speichern.
            fachgruppeService.saveAndFlush(fachgruppe);

            return new ResponseEntity<>(fachgruppe, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachgruppe nicht gefunden");
        }
    }

    /**
     * Validiert das übergebene Fachbereich-Objekt.
     * 
     * @param fachgruppe das zu validierende Fachbereich-Objekt
     * @return eine Liste von Fehlermeldungen, leer wenn keine Validierungsfehler
     *         vorliegen
     */
    List<String> validateFachgruppe(Fachgruppe fachgruppe) {
        List<String> errors = new ArrayList<>();

        // Namensprüfung
        if (fachgruppe.getName() == null || fachgruppe.getName().isEmpty()) {
            errors.add("Das Feld 'Name' ist erforderlich.");
        }

        // Längenprüfung
        if (fachgruppe.getName() != null && fachgruppe.getName().length() < 2) {
            errors.add("Das Feld 'Name' muss mindestens 2 Zeichen lang sein.");
        }

        // Überprüfen, Kürzel gesetzt ist
        if (fachgruppe.getKuerzel() == null) {
            errors.add("Kuerzel ist nicht gesetzt");
        }

        // Überprüfen, ob das Feld Referent gesetzt ist
        if (fachgruppe.getReferent() == null) {
            errors.add("Referent ist nicht gesetzt.");
        }

        // Überprüfen, ob das Feld Stellvertreter gesetzt ist
        if (fachgruppe.getStellvertreter() == null) {
            errors.add("Stellvertreter ist nicht gesetzt.");
        }

        // Überprüfen, ob das Feld Fachbereich gesetzt ist
        if (fachgruppe.getFachbereich() == null) {
            errors.add("Fachbereich ist nicht gesetzt.");
        }

        // Überprüfen, ob Referent und Stellvertreter identisch sind
        if (fachgruppe.getReferent() != null && fachgruppe.getStellvertreter() != null
                && fachgruppe.getReferent().getId() == fachgruppe.getStellvertreter().getId()) {
            errors.add("Referent und Stellvertreter dürfen nicht identisch sein");
        }

        return errors;
    }
}