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
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controller-Klasse für die Verwaltung von Studiengängen.
 */
@RequestMapping("/studiengang")
@RestController
public class StudiengangController {

    // Deklarierung Services
    @Autowired
    StudiengangService studiengangService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    FachbereichService fachbereichService;

    @Autowired
    NutzerService nutzerService;

    /**
     * Methode zum Abrufen eines einzelnen Studiengang anhand seiner ID.
     * 
     * @param id Die ID des zu holenden Studiengangs.
     * @return Der gefundene Studiengang.
     * @throws ResponseStatusException Falls der Studiengang nicht gefunden wird,
     *                                 wird ein 404 Fehler zurückgegeben.
     */
    @GetMapping("/{id}")
    public Studiengang getOne(@PathVariable String id) {
        // Studiengang anhand der ID abrufen
        Optional<Studiengang> studiengang = studiengangService.getStudiengang(Long.parseLong(id));

        // Überprüfen, ob der Studiengang vorhanden ist
        if (studiengang.isPresent()) {
            // Wenn der Studiengang vorhanden ist, das Studiengang-Objekt aus dem Optional
            // extrahieren
            Studiengang studiengangObject = studiengang.get();

            // Die IDs für Leiter, stellvertretenden Leiter und Fachbereich im
            // Studiengang-Objekt setzen
            studiengangObject.setLeiterId(studiengangObject.getLeiter().getId());
            studiengangObject.setStellvertreterId(studiengangObject.getStellvertretenderLeiter().getId());
            studiengangObject.setFachbereichId(studiengangObject.getFachbereich().getId());

            // Das Studiengang-Objekt zurückgeben
            return studiengangObject;
        } else {
            // Falls der Studiengang nicht gefunden wird, einen 404 Fehler zurückgeben
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Studiengänge.
     * 
     * @param response HTTP-Servlet-Antwort, um den Content-Range-Header zu setzen.
     * @return Eine Liste aller Studiengänge.
     */
    @GetMapping("")
    public List<Studiengang> getAll(HttpServletResponse response) {
        // Alle Studiengänge abrufen
        List<Studiengang> list = studiengangService.getStudiengaenge();

        // Für jeden Studiengang in der Liste die IDs für Leiter, stellvertretenden
        // Leiter und Fachbereich setzen
        list.forEach(studiengang -> {
            studiengang.setLeiterId(studiengang.getLeiter().getId());
            studiengang.setStellvertreterId(studiengang.getStellvertretenderLeiter().getId());
            studiengang.setFachbereichId(studiengang.getFachbereich().getId());
        });

        // Den Content-Range-Header der HTTP-Antwort setzen, um den Bereich der
        // zurückgegebenen Studiengänge anzugeben
        response.setHeader("Content-Range", "1-" + list.size());

        // Die Liste der Studiengänge zurückgeben
        return list;
    }

    /**
     * Methode zum Erstellen eines neuen Studiengang.
     * 
     * @param studiengang Der zu erstellende Studiengang.
     * @return Der erstellte Studiengang.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist,
     *                                 wird ein 401 Fehler zurückgegeben.
     *                                 Falls der Leiter, der Stellvertreter oder der
     *                                 Fachbereich nicht gefunden werden,
     *                                 wird ein 404 Fehler zurückgegeben.
     */
    @PostMapping("")
    public ResponseEntity<Studiengang> createFachbereich(@RequestBody Studiengang studiengang) {
        // Authentifizierung des Benutzers über den SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Benutzerinformationen abrufen und sicherstellen, dass der Benutzer
        // autorisiert ist
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        // Überprüfen, ob der Benutzer die erforderliche Rolle hat, um die Operation
        // auszuführen
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Den Mitarbeiter für den Leiter des Studiengangs abrufen oder eine Ausnahme
        // auslösen, wenn nicht gefunden
        Mitarbeiter leiter = mitarbeiterService
                .getMitarbeiter(studiengang.getLeiterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leiter nicht gefunden"));

        // Den Mitarbeiter für den stellvertretenden Leiter des Studiengangs abrufen
        // oder eine Ausnahme auslösen, wenn nicht gefunden
        Mitarbeiter stellvertretenderLeiter = mitarbeiterService
                .getMitarbeiter(studiengang.getStellvertreterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter nicht gefunden"));

        // Den Fachbereich des Studiengangs abrufen oder eine Ausnahme auslösen, wenn
        // nicht gefunden
        Fachbereich fachbereich = fachbereichService
                .getFachbereich(studiengang.getFachbereichId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachbereich nicht gefunden"));

        if (!(fachbereich.getReferent().getId() == nutzer.getId()
                || fachbereich.getStellvertreter().getId() == nutzer.getId() || nutzer.getRole().equals("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Die Informationen für Leiter, stellvertretenden Leiter und Fachbereich im
        // Studiengang setzen
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertretenderLeiter);
        studiengang.setFachbereich(fachbereich);

        // Den aktualisierten Studiengang speichern und flushen
        studiengangService.saveAndFlush(studiengang);

        // Eine Antwort mit dem erstellten Studiengang und dem Status "Created"
        // zurückgeben
        return new ResponseEntity<>(studiengang, HttpStatus.CREATED);
    }

    /**
     * Methode zum Aktualisieren eines vorhandenen Studiengang.
     * 
     * @param id                Die ID des zu aktualisierenden Studiengangs.
     * @param updateStudiengang Der aktualisierte Studiengang.
     * @return Der aktualisierte Studiengang.
     * @throws ResponseStatusException Falls der Studiengang nicht gefunden wird,
     *                                 wird ein 404 Fehler zurückgegeben.
     *                                 Falls der Leiter oder der Stellvertreter
     *                                 nicht gefunden wird, wird ein 404 Fehler
     *                                 zurückgegeben.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Studiengang> updateStudiengang(@PathVariable String id,
            @RequestBody Studiengang updateStudiengang) {
        // Vorhandenen Studiengang anhand der ID abrufen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Benutzerinformationen abrufen und sicherstellen, dass der Benutzer
        // autorisiert ist
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        // Überprüfen, ob der Benutzer die erforderliche Rolle hat, um die Operation
        // auszuführen
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        Optional<Studiengang> existingStudiengang = studiengangService.getStudiengang(Long.parseLong(id));

        // Überprüfen, ob der Studiengang vorhanden ist
        if (existingStudiengang.isPresent()) {
            // Wenn der Studiengang vorhanden ist, das Studiengang-Objekt aus dem Optional
            // extrahieren
            Studiengang studiengang = existingStudiengang.get();

            // Überprüfen, ob der Benutzer der Leiter oder der stellvertretende Leiter des
            // Studiengangs ist oder ein Administrator ist
            if (!(studiengang.getLeiter().getId() == nutzer.getId()
                    || studiengang.getStellvertretenderLeiter().getId() == nutzer.getId()
                    || nutzer.getRole().equals("ADMIN"))) {
                // Falls der Benutzer nicht der Leiter oder der stellvertretende Leiter des
                // Studiengangs ist oder kein Administrator ist, einen 401 Fehler zurückgeben
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
            }

            // Den Mitarbeiter für den Leiter des aktualisierten Studiengangs abrufen
            Mitarbeiter leiter = mitarbeiterService
                    .getMitarbeiter(updateStudiengang.getLeiterId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leiter not found"));

            // Den Mitarbeiter für den stellvertretenden Leiter des aktualisierten
            // Studiengangs abrufen
            Mitarbeiter stellvertretenderLeiter = mitarbeiterService
                    .getMitarbeiter(updateStudiengang.getStellvertreterId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stellvertreter not found"));

            // Den Leiter und den stellvertretenden Leiter des Studiengangs aktualisieren
            studiengang.setLeiter(leiter);
            studiengang.setStellvertretenderLeiter(stellvertretenderLeiter);

            // Den aktualisierten Studiengang speichern und flushen
            studiengangService.saveAndFlush(studiengang);

            // Eine Antwort mit dem aktualisierten Studiengang und dem Status "OK"
            // zurückgeben
            return new ResponseEntity<>(studiengang, HttpStatus.OK);
        } else {
            // Falls der Studiengang nicht gefunden wird, einen 404 Fehler zurückgeben
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Validiert das übergebene Fachbereich-Objekt.
     * 
     * @param studiengang das zu validierende Fachbereich-Objekt
     * @return eine Liste von Fehlermeldungen, leer wenn keine Validierungsfehler
     *         vorliegen
     */
    public static List<String> validateStudiengang(Studiengang studiengang) {
        List<String> errors = new ArrayList<>();

        // Namensprüfung
        if (studiengang.getName() == null || studiengang.getName().isEmpty()) {
            errors.add("Das Feld 'Name' ist erforderlich.");
        }

        // Längenprüfung
        if (studiengang.getName() != null && studiengang.getName().length() < 2) {
            errors.add("Das Feld 'Name' muss mindestens 2 Zeichen lang sein.");
        }

        // Überprüfen, ob Abschluss ausgewählt ist
        if (studiengang.getAbschluss() == null) {
            errors.add("Abschluss ist nicht gesetzt");

        }

        // Überprüfen, ob Regelstudienzeit ausgewählt ist
        if (studiengang.getRegelstudienzeit() <= 0) {
            errors.add("Regelstudienzeit muss eine positive Zahl sein und darf nicht 0 oder negativ sein.");
        }

        // Überprüfen, ob Leiter ausgewählt ist
        if (studiengang.getLeiter() == null) {
            errors.add("Leiter ist nicht gesetzt");
        }

        // Überprüfen, ob Stellvertreter ausgewählt ist
        if (studiengang.getStellvertretenderLeiter() == null) {
            errors.add("Stellvertreter ist nicht gesetzt");
        }

        // Überprüfen, ob Fachbereich ausgewählt ist
        if (studiengang.getFachbereich() == null) {
            errors.add("Fachbereich ist nicht gesetzt");
        }

        return errors;
    }
}
