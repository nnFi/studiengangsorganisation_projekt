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

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für die Verwaltung von Pruefungen.
 */
@RequestMapping("/pruefung")
@RestController
public class PruefungController {

    // Deklarierung Services
    @Autowired
    PruefungService pruefungService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @Autowired
    ModulService modulService;

    /**
     * Methode zum Abrufen einer einzelnen Prüfung anhand ihrer ID.
     * 
     * @param id Die ID der zu holenden Prüfung.
     * @return Die gefundene Prüfung.
     * @throws ResponseStatusException Falls die Prüfung nicht gefunden wird, wird
     *                                 ein 404 Fehler zurückgegeben.
     */
    @GetMapping("/{id}")
    public Pruefung getOne(@PathVariable String id) {
        // Prüfung anhand der ID abrufen
        Optional<Pruefung> pruefung = pruefungService.getPruefung(id);

        // Überprüfen, ob die Prüfung vorhanden ist
        if (pruefung.isPresent()) {
            // Wenn die Prüfung vorhanden ist, Pruefung-Objekt aus dem Optional extrahieren
            Pruefung pruefungObject = pruefung.get();

            // Die IDs der zugehörigen Prüfungsordnung und des Moduls setzen
            pruefungObject.setPruefungsordnungId(pruefungObject.getPruefungsordnung().getId());
            pruefungObject.setModulId(pruefungObject.getModul().getId());

            // Das Pruefung-Objekt zurückgeben
            return pruefungObject;
        } else {
            // Falls die Prüfung nicht gefunden wird, einen 404 Fehler zurückgeben
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Prüfungen.
     * 
     * @param response HTTP-Servlet-Antwort, um den Content-Range-Header zu setzen.
     * @return Eine Liste aller Prüfungen.
     */
    @GetMapping("")
    public List<Pruefung> getAll(HttpServletResponse response) {
        // Alle Prüfungen abrufen
        List<Pruefung> list = pruefungService.getPruefungen();

        // Für jede Prüfung in der Liste die Prüfungsordnung-ID und die Modul-ID setzen
        list.forEach(pruefung -> {
            pruefung.setPruefungsordnungId(pruefung.getPruefungsordnung().getId());
            pruefung.setModulId(pruefung.getModul().getId());
        });

        // Den Content-Range-Header der HTTP-Antwort setzen, um den Bereich der
        // zurückgegebenen Prüfungen anzugeben
        response.setHeader("Content-Range", "1-" + list.size());

        // Die Liste der Prüfungen zurückgeben
        return list;
    }

    /**
     * Methode zum Erstellen einer neue Prüfung.
     * 
     * @param pruefung Die zu erstellende Prüfung.
     * @return Die erstellte Prüfung.
     * @throws ResponseStatusException Falls der Nutzer nicht authorisiert ist, wird
     *                                 ein 401 Fehler zurückgegeben.
     *                                 Falls die Prüfungsordnung nicht gefunden
     *                                 wird, wird ein 404 Fehler zurückgegeben.
     *                                 Falls das Modul nicht gefunden wird, wird ein
     *                                 404 Fehler zurückgegeben.
     *                                 Falls die Prüfung bereits existiert, wird ein
     *                                 409 Fehler zurückgegeben.
     *                                 Falls die Prüfungsordnung bereits freigegeben
     *                                 wurde, wird ein 409 Fehler zurückgegeben.
     */
    @PostMapping("")
    public ResponseEntity<Pruefung> createPruefung(@RequestBody Pruefung pruefung) {
        // Authentifizierung des Benutzers
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Benutzerinformationen aus dem NutzerService abrufen und sicherstellen, dass
        // der Benutzer autorisiert ist
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User nicht authorisiert"));

        // Überprüfen, ob der Benutzer die erforderliche Rolle hat
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User nicht authorisiert");
        }

        // TODO: Nur der Studiengangsleiter, dessen Vertreter, der Fachbereichsleiter
        // oder der Administrator können Prüfungen erstellen

        // Die Prüfungsordnung für die zu erstellende Prüfung abrufen und sicherstellen,
        // dass sie existiert
        Pruefungsordnung pruefungsordnung = pruefungsordnungService
                .getPruefungsordnung(pruefung.getPruefungsordnungId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pruefungsordnung nicht gefunden"));

        // Überprüfen, ob eine Prüfung für dasselbe Modul bereits in der Prüfungsordnung
        // existiert
        for (Pruefung p : pruefungsordnung.getPruefungen()) {
            if (p.getModulId().equals(pruefung.getModulId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Pruefung existiert bereits");
            }
        }

        // Das Modul für die zu erstellende Prüfung abrufen und sicherstellen, dass es
        // existiert
        Modul modul = modulService
                .getModul(pruefung.getModulId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        // Die Prüfungsordnung und das Modul für die Prüfung setzen
        pruefung.setPruefungsordnung(pruefungsordnung);
        pruefung.setModul(modul);

        // Die Prüfung speichern, wenn die Prüfungsordnung noch nicht freigegeben wurde,
        // andernfalls eine Ausnahme auslösen
        if (!pruefungsordnung.isFreigegeben()) {
            pruefungService.saveAndFlush(pruefung);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pruefungsordnung wurde bereits freigegeben");
        }

        // Eine Antwort mit der erstellten Prüfung und dem Status "Created" zurückgeben
        return new ResponseEntity<>(pruefung, HttpStatus.CREATED);
    }
}