package com.projekt.studiengangsorganisation.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 
 * @author Finn Plassmeier
 */
@RequestMapping("/api/pruefungsordnung")
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
     * 
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
     * Methode zum Abrufen aller Prüfungsordnungen oder einer spezifischen Auswahl
     * von Prüfungsordnungen
     * basierend auf einem Filter.
     * 
     * @param response Der HTTP-Response, in dem der Content-Range Header gesetzt
     *                 wird, um die Anzahl der zurückgegebenen Prüfungsordnungen
     *                 anzugeben.
     * @param filter   Ein optionaler String, der als JSON-Objekt interpretiert
     *                 wird. Derzeit unterstützte Filter sind: - studiengang: Eine
     *                 Liste von Studiengang-IDs.
     * @return Eine Liste aller Prüfungsordnungen oder eine Liste der
     *         Prüfungsordnungen, die den
     *         Filterkriterien entsprechen.
     * @throws JsonMappingException    Wenn es ein Problem beim Parsen des
     *                                 Filter-Strings als JSON gibt.
     * @throws JsonProcessingException Wenn es ein Problem beim Verarbeiten des
     *                                 Filter-Strings als JSON gibt.
     */
    @GetMapping("")
    public List<Pruefungsordnung> getAll(HttpServletResponse response, @RequestParam(required = false) String filter)
            throws JsonMappingException, JsonProcessingException {
        List<Pruefungsordnung> list;

        if (filter != null && !filter.isEmpty() && !filter.equals("{}")) {
            // Den Filter als JSON-Objekt parsen
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> rawMap = mapper.readValue(filter, new TypeReference<Map<String, Object>>() {
            });

            Map<String, List<Long>> filterMap = new HashMap<>();

            for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof List) {
                    // Wenn es sich um eine Liste handelt, konvertiere sie in eine Liste von Longs
                    filterMap.put(key, mapper.convertValue(value, new TypeReference<List<Long>>() {
                    }));
                } else {
                    // Wenn es sich um einen einzelnen Wert handelt, konvertiere ihn in einen Long
                    // und füge ihn der Liste hinzu
                    Long singleValue = mapper.convertValue(value, Long.class);
                    filterMap.put(key, Collections.singletonList(singleValue));
                }
            }

            if (filterMap.containsKey("studiengang")) {
                list = pruefungsordnungService.getPruefungsordnungenByStudiengangIds(filterMap.get("studiengang"));
            } else {
                list = pruefungsordnungService.getPruefungsordnungen();
            }

        } else {
            // Alle Prüfungsordnungen abrufen
            list = pruefungsordnungService.getPruefungsordnungen();
        }

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
     * 
     * @param pruefungsordnung Die zu erstellende Pruefungsordnung.
     * @return Die erstellte Pruefungsordnung.
     */
    @PostMapping("")
    public ResponseEntity<Pruefungsordnung> createPruefungsordnung(@RequestBody Pruefungsordnung pruefungsordnung) {
        // Den aktuellen Benutzer über die Authentifizierungsinformationen abrufen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Den Nutzer anhand des Benutzernamens aus dem Authentifizierungsobjekt abrufen
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        // Überprüft ob der Benutzer eine Prüfung erstellen darf und gibt im Fehlerfall
        // 401 zurück
        if (!(nutzer.getRole().equals("ADMIN")
                || nutzer.getRole().equals("MITARBEITER")
                        && (pruefungsordnung.getStudiengang().getLeiter().getId() == nutzer.getId()
                                || pruefungsordnung.getStudiengang().getStellvertretenderLeiter().getId() == nutzer
                                        .getId()
                                || pruefungsordnung.getStudiengang().getFachbereich().getReferent().getId() == nutzer
                                        .getId()
                                || pruefungsordnung.getStudiengang().getFachbereich().getStellvertreter()
                                        .getId() == nutzer.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Den Studiengang anhand der ID aus der Prüfungsordnungsinformationen abrufen
        Studiengang studiengang = studiengangService
                .getStudiengang(pruefungsordnung.getStudiengangId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Studiengang nicht gefunden"));

        // Den abgerufenen Studiengang der Prüfungsordnung zuweisen
        pruefungsordnung.setStudiengang(studiengang);

        // Überprüfen, ob es bereits eine Prüfungsordnung mit dem gleichen Studiengang
        // und
        // Version gibt
        if (pruefungsordnungService
                .getPruefungsordnung(pruefungsordnung.getVersion(), pruefungsordnung.getStudiengang()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pruefungsordnung bereits vorhanden");
        }

        // Die aktualisierte Prüfungsordnung speichern und HTTP-Statuscode 201 (Created)
        // zurückgeben
        pruefungsordnungService.saveAndFlush(pruefungsordnung);
        return new ResponseEntity<>(pruefungsordnung, HttpStatus.CREATED);
    }

    /**
     * Methode zum Aktualisieren einer Pruefungsordnung anhand ihrer ID.
     * 
     * @param id                      Die ID der zu aktualisierenden
     *                                Pruefungsordnung.
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
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

            // Überprüft ob der Benutzer eine Prüfung bearbeiten darf und gibt im Fehlerfall
            // 401 zurück
            if (!(nutzer.getRole().equals("ADMIN")
                    || nutzer.getRole().equals("MITARBEITER")
                            && (pruefungsordnung.getStudiengang().getLeiter().getId() == nutzer.getId()
                                    || pruefungsordnung.getStudiengang().getStellvertretenderLeiter().getId() == nutzer
                                            .getId()
                                    || pruefungsordnung.getStudiengang().getFachbereich().getReferent()
                                            .getId() == nutzer.getId()
                                    || pruefungsordnung.getStudiengang().getFachbereich().getStellvertreter()
                                            .getId() == nutzer.getId()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
            }

            // Aktualisieren der Prüfungsordnung, sofern sie noch nicht veröffentlicht wurde
            if (!pruefungsordnung.isFreigegeben()) {
                pruefungsordnung.setFreigegeben(updatedPruefungsordnung.isFreigegeben());
            }
            if (!pruefungsordnung.isAuslaufend()) {
                pruefungsordnung.setAuslaufend(updatedPruefungsordnung.isAuslaufend());
            } else {
                // Fehlermeldung zurückgeben, wenn versucht wird, eine bereits veröffentlichte
                // Prüfungsordnung zu bearbeiten
                throw new ResponseStatusException(HttpStatus.NOT_MODIFIED,
                        "Bereits veröffentlichte Prüfungsordnungen können nicht bearbeitet werden");
            }

            // Die aktualisierten Daten der Prüfungsordnung speichern und aktualisiertes
            // Objekt zurückgeben
            pruefungsordnungService.saveAndFlush(pruefungsordnung);
            return new ResponseEntity<>(pruefungsordnung, HttpStatus.OK);
        } else {
            // Fehlermeldung zurückgeben, wenn die Prüfungsordnung nicht gefunden wurde
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pruefungsordnung nicht gefunden");
        }
    }

    /**
     * Methode zum Validieren einer Pruefungsordnung.
     * 
     * @param pruefungsordnung Die zu validierende Pruefungsordnung.
     * @return Eine Liste von Fehlern, die bei der Validierung aufgetreten sind.
     */
    public List<String> validatePruefungsordnung(Pruefungsordnung pruefungsordnung) {
        // Liste für Fehlermeldungen erstellen
        List<String> errors = new ArrayList<>();

        // Überprüfen, ob die Version der Prüfungsordnung gesetzt ist
        if (pruefungsordnung.getVersion() == null || pruefungsordnung.getVersion().isEmpty()
                || pruefungsordnung.getVersion().isBlank()) {
            // Fehlermeldung hinzufügen
            errors.add("Version der Pruefungsordnung fehlt");
        }

        // Überprüfen, ob die ID des zugehörigen Studiengangs gesetzt ist
        if (pruefungsordnung.getStudiengangId() == null) {
            // Fehlermeldung hinzufügen
            errors.add("ID des zugehörigen Studiengangs fehlt");
        }

        // Die Liste der Fehlermeldungen zurückgeben
        return errors;
    }
}