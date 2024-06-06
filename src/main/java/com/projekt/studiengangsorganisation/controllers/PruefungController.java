package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * @param response Der HTTP-Response, in dem der Content-Range Header gesetzt
     *                 wird, um die Anzahl der zurückgegebenen Prüfungen anzugeben.
     * @param filter   Ein optionaler String, der als JSON-Objekt interpretiert
     *                 wird. Wenn der Filter "id" enthält, werden nur die Prüfungen
     *                 mit
     *                 den entsprechenden IDs zurückgegeben.
     * @return Eine Liste aller Prüfungen oder eine Liste der Prüfungen, die den
     *         Filterkriterien entsprechen.
     * @throws JsonMappingException    Wenn es ein Problem beim Parsen des
     *                                 Filter-Strings als JSON gibt.
     * @throws JsonProcessingException Wenn es ein Problem beim Verarbeiten des
     *                                 Filter-Strings als JSON gibt.
     */

    @GetMapping("")
    public List<Pruefung> getAll(HttpServletResponse response, @RequestParam(required = false) String filter)
            throws JsonMappingException, JsonProcessingException {
        List<Pruefung> list;

        if (filter != null && !filter.isEmpty()) {
            // Den Filter als JSON-Objekt parsen
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Long>> filterMap = mapper.readValue(filter, new TypeReference<Map<String, List<Long>>>() {
            });

            // Wenn ein Filter bereitgestellt wird und "id" enthält, nur diese Prüfungen
            // abrufen
            if (filterMap.containsKey("id")) {
                list = pruefungService.getPruefungenByIds(filterMap.get("id"));
            } else {
                list = pruefungService.getPruefungen();
            }
        } else {
            // Wenn kein Filter bereitgestellt wird, alle Prüfungen abrufen
            list = pruefungService.getPruefungen();
        }

        list.forEach(pruefung -> {
            pruefung.setPruefungsordnungId(pruefung.getPruefungsordnung().getId());
            pruefung.setModulId(pruefung.getModul().getId());
        });

        // Setze den Content-Range Header im Response, um die Anzahl der Prüfungen
        // anzugeben
        response.setHeader("Content-Range", "1-" + list.size());

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

        // Überprüft ob der Benutzer eine Prüfung erstellen darf und gibt im Fehlerfall
        // 401 zurück
        if (!(nutzer.getRole().equals("ADMIN")
                || nutzer.getRole().equals("Mitarbeiter")
                        && (pruefung.getPruefungsordnung().getStudiengang().getLeiter().getId() == nutzer.getId()
                                || pruefung.getPruefungsordnung().getStudiengang().getStellvertretenderLeiter().getId() == nutzer
                                        .getId()
                                || pruefung.getPruefungsordnung().getStudiengang().getFachbereich()
                                        .getReferent().getId() == nutzer.getId()
                                || pruefung.getPruefungsordnung().getStudiengang().getFachbereich()
                                        .getStellvertreter().getId() == nutzer.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User nicht authorisiert");
        }

        // Die Prüfungsordnung für die zu erstellende Prüfung abrufen und sicherstellen,
        // dass sie existiert
        Pruefungsordnung pruefungsordnung = pruefungsordnungService
                .getPruefungsordnung(pruefung.getPruefungsordnungId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pruefungsordnung nicht gefunden"));

        // Überprüfen, ob eine Prüfung für dasselbe Modul bereits in der Prüfungsordnung
        // existiert
        for (Pruefung p : pruefungsordnung.getPruefungen()) {
            if (p.getModul().getId().equals(pruefung.getModulId())) {
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