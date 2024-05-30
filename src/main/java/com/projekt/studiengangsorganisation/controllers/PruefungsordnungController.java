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


import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;
import com.projekt.studiengangsorganisation.service.StudiengangService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für Pruefungsordnung-Ressourcen.
 */
@RequestMapping("/pruefungsordnung")
@RestController
public class PruefungsordnungController {

    // Deklarierung Services
    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    PruefungService pruefungService;

    @Autowired
    StudiengangService studiengangService;

    /**
     * Methode zum Abrufen einer Pruefungsordnung anhand ihrer ID.
     * @param id Die ID der zu findenden Pruefungsordnung.
     * @return Die gefundene Pruefungsordnung.
     */
    @GetMapping("/{id}")
    public Pruefungsordnung getOne(@PathVariable String id) {
        // Die Prüfungsordnung mit der angegebenen ID aus dem Service abrufen
        Optional<Pruefungsordnung> pruefungsordnung = pruefungsordnungService.getPruefungsordnung(Long.parseLong(id));

        // Überprüfen, ob die Prüfungsordnung vorhanden ist
        if (pruefungsordnung.isPresent()) {
            // Die vorhandene Prüfungsordnung aus dem Optional extrahieren
            Pruefungsordnung pruefungsordnungObject = pruefungsordnung.get();

            // Die ID des zugehörigen Studiengangs in der Prüfungsordnung setzen
            pruefungsordnungObject.setStudiengangId(pruefungsordnungObject.getStudiengang().getId());

            // Die Prüfungsordnung zurückgeben
            return pruefungsordnungObject;
        } else {
            // Fehlermeldung zurückgeben, wenn die Prüfungsordnung nicht gefunden wurde
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Pruefungsordnungen.
     * @param response Das HTTP-Response-Objekt.
     * @return Eine Liste aller Pruefungsordnungen.
     */
    @GetMapping("")
    public List<Pruefungsordnung> getAll(HttpServletResponse response) {
        // Alle Prüfungsordnungen abrufen
        List<Pruefungsordnung> list = pruefungsordnungService.getPruefungsordnungen();

        // Für jede Prüfungsordnung in der Liste
        list.forEach(pruefungsordnung -> {
            // Die ID des zugehörigen Studiengangs in der Prüfungsordnung setzen
            pruefungsordnung.setStudiengangId(pruefungsordnung.getStudiengang().getId());
        });

        // Die Anzahl der Elemente im Response-Header setzen
        response.setHeader("Content-Range", "1-" + list.size());

        // Die Liste der Prüfungsordnungen zurückgeben
        return list;
    }

    /**
     * Methode zum Erstellen einer neuen Pruefungsordnung.
     * @param pruefungsordnung Die zu erstellende Pruefungsordnung.
     * @return Die erstellte Pruefungsordnung.
     */
    @PostMapping("")
    public ResponseEntity<Pruefungsordnung> createFachbereich(@RequestBody Pruefungsordnung pruefungsordnung) {
        // Den aktuellen Benutzer über die Authentifizierungsinformationen abrufen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Den Nutzer anhand des Benutzernamens aus dem Authentifizierungsobjekt abrufen
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        // Überprüfen, ob der Benutzer die Berechtigung hat, die Aktion auszuführen
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Den Studiengang anhand der ID aus der Prüfungsordnungsinformationen abrufen
        Studiengang studiengang = studiengangService
                .getStudiengang(pruefungsordnung.getStudiengangId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Studiengang nicht gefunden"));

        // Den abgerufenen Studiengang der Prüfungsordnung zuweisen
        pruefungsordnung.setStudiengang(studiengang);

        // Die aktualisierte Prüfungsordnung speichern und HTTP-Statuscode 201 (Created) zurückgeben
        pruefungsordnungService.saveAndFlush(pruefungsordnung);
        return new ResponseEntity<>(pruefungsordnung, HttpStatus.CREATED);
    }

    /**
     * Methode zum Aktualisieren einer Pruefungsordnung anhand ihrer ID.
     * @param id Die ID der zu aktualisierenden Pruefungsordnung.
     * @param updatedPruefungsordnung Die aktualisierte Pruefungsordnung.
     * @return Die aktualisierte Pruefungsordnung.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pruefungsordnung> updatePruefungsordnung(@PathVariable String id,
            @RequestBody Pruefungsordnung updatedPruefungsordnung) {
        // Prüfen, ob die angegebene Prüfungsordnung vorhanden ist
        Optional<Pruefungsordnung> existingPruefungsordnung = pruefungsordnungService
        .getPruefungsordnung(Long.parseLong(id));

        // Wenn die Prüfungsordnung vorhanden ist
        if (existingPruefungsordnung.isPresent()) {
        // Die vorhandene Prüfungsordnung aus dem Optional extrahieren
        Pruefungsordnung pruefungsordnung = existingPruefungsordnung.get();

        // Überprüfen, ob der aktuelle Benutzer ein Administrator ist
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht authorisiert"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht authorisiert");
        }

        // Aktualisieren der Prüfungsordnung, sofern sie noch nicht veröffentlicht wurde
        if (!pruefungsordnung.isFreigegeben()) {
        pruefungsordnung.setFreigegeben(updatedPruefungsordnung.isFreigegeben());
        }
        if (!pruefungsordnung.isAuslaufend()) {
        pruefungsordnung.setAuslaufend(updatedPruefungsordnung.isAuslaufend());
        } else {
        // Fehlermeldung zurückgeben, wenn versucht wird, eine bereits veröffentlichte Prüfungsordnung zu bearbeiten
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED,
                "Bereits veröffentlichte Prüfungsordnungen können nicht bearbeitet werden");
        }

        // Die aktualisierten Daten der Prüfungsordnung speichern und aktualisiertes Objekt zurückgeben
        pruefungsordnungService.saveAndFlush(pruefungsordnung);
        return new ResponseEntity<>(pruefungsordnung, HttpStatus.OK);
        } else {
        // Fehlermeldung zurückgeben, wenn die Prüfungsordnung nicht gefunden wurde
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pruefungsordnung nicht gefunden");
        }
    }
}