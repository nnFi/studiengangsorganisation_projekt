package com.projekt.studiengangsorganisation.controllers;

import java.util.ArrayList;
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

/**
 * Controller-Klasse für die Verwaltung von Modulen.
 */
@RequestMapping("/modul")
@RestController
public class ModulController {

    // Deklarierung Services
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

    /**
     * Methode zum Abrufen eines Moduls anhand seiner ID.
     * 
     * @param id Die ID des Moduls.
     * @return Das Modul, falls gefunden.
     * @throws ResponseStatusException Falls das Modul nicht gefunden wird.
     */
    @GetMapping("/{id}")
    public Modul getOne(@PathVariable String id) {
        // Versuche, das Modul mit der angegebenen ID zu erhalten
        Optional<Modul> modul = modulService.getModul(Long.parseLong(id));

        // Wenn das Modul gefunden wurde
        if (modul.isPresent()) {
            Modul modulObject = modul.get();
            // Setze die IDs der zugehörigen Entitäten für eine kompakte Antwort
            modulObject.setModulbeauftragterId(modulObject.getModulbeauftragter().getId());
            modulObject.setFachgruppeId(modulObject.getFachgruppe().getId());
            modulObject.setModulgruppeId(modulObject.getModulgruppe().getId());
            return modul.get();
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Module oder einer spezifischen Auswahl von Modulen
     * basierend auf einem Filter.
     * 
     * @param response Der HTTP-Response, in dem der Content-Range Header gesetzt
     *                 wird, um die Anzahl der zurückgegebenen Module anzugeben.
     * @param filter   Ein optionaler String, der als JSON-Objekt interpretiert
     *                 wird. Wenn der Filter "id" enthält, werden nur die Module mit
     *                 den entsprechenden IDs zurückgegeben.
     * @return Eine Liste aller Module oder eine Liste der Module, die den
     *         Filterkriterien entsprechen.
     * @throws JsonMappingException    Wenn es ein Problem beim Parsen des
     *                                 Filter-Strings als JSON gibt.
     * @throws JsonProcessingException Wenn es ein Problem beim Verarbeiten des
     *                                 Filter-Strings als JSON gibt.
     */
    @GetMapping("")
    public List<Modul> getAll(HttpServletResponse response, @RequestParam(required = false) String filter)
            throws JsonMappingException, JsonProcessingException {
        List<Modul> list;

        if (filter != null && !filter.isEmpty() && !filter.equals("{}")) {
            // Den Filter als JSON-Objekt parsen
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Long>> filterMap = mapper.readValue(filter, new TypeReference<Map<String, List<Long>>>() {
            });

            // Wenn ein Filter bereitgestellt wird und "id" enthält, nur diese Prüfungen
            // abrufen
            if (filterMap.containsKey("id")) {
                list = modulService.getModuleByIds(filterMap.get("id"));
            } else {
                list = modulService.getModule();
            }
        } else {
            // Wenn kein Filter bereitgestellt wird, alle Module abrufen
            list = modulService.getModule();
        }

        // Setze die IDs der zugehörigen Entitäten
        list.forEach(modul -> {
            modul.setModulbeauftragterId(modul.getModulbeauftragter().getId());
            modul.setFachgruppeId(modul.getFachgruppe().getId());
            modul.setModulgruppeId(modul.getModulgruppe().getId());
        });

        // Setze den Content-Range Header im Response, um die Anzahl der Module
        // anzugeben
        response.setHeader("Content-Range", "1-" + list.size());

        return list;
    }

    /**
     * Methode zum Erstellen eines neuen Moduls.
     * 
     * @param modul Das Modul, das erstellt werden soll.
     * @return Eine ResponseEntity, die das erstellte Modul und den HTTP-Statuscode
     *         enthält.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist oder
     *                                 eine Entität nicht gefunden wird.
     */
    @PostMapping("")
    public ResponseEntity<Modul> createModul(@RequestBody Modul modul) {
        // Überprüfe, ob der Benutzer autorisiert ist
        // Erlaubt sind MITARBEITER und ADMIN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            // Wenn der Benutzer nicht autorisiert ist, wirf einen Fehler 401 - Nicht
            // autorisiert
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Hole die Fachgruppe
        Fachgruppe fachgruppe = fachgruppeService
                .getFachgruppe(modul.getFachgruppeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachgruppe nicht gefunden"));

        // Überprüfe, ob der Benutzer berechtigt ist, das Modul für die Fachgruppe zu
        // erstellen. Nur der Fachbereichsreferent/stellvertreter oder der Admin sind
        // berechtigt,
        // Module zu erstellen.
        if (fachgruppe.getFachbereich().getReferentId() != nutzer.getId()
                && fachgruppe.getFachbereich().getStellvertreterId() != nutzer.getId()
                && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Hole den Modulbeauftragten und die Modulgruppe
        Mitarbeiter modulbeauftragter = mitarbeiterService
                .getMitarbeiter(modul.getModulbeauftragterId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modulbeauftragter nicht gefunden"));

        Modulgruppe modulgruppe = modulgruppeService
                .getModulgruppe(modul.getModulgruppeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modulgruppe nicht gefunden"));

        // Setze die zugehörigen Entitäten und speichere das Modul
        modul.setFachgruppe(fachgruppe);
        modul.setModulbeauftragter(modulbeauftragter);
        modul.setModulgruppe(modulgruppe);
        Modul savedModul = modulService.saveAndFlush(modul);

        // Generiere die Modulnummer und setze sie
        String modulnummer = (fachgruppe.getFachbereich().getId() + " " + fachgruppe.getKuerzel() + " "
                + savedModul.getId());
        savedModul.setModulnummer(modulnummer);

        // Validierungslogik für die Eingabefelder
        List<String> errors = validateModul(modul);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }

        // Speichere das Modul mit der Modulnummer
        modulService.saveAndFlush(savedModul);

        return new ResponseEntity<>(savedModul, HttpStatus.CREATED);
    }

    /**
     * Methode zum Aktualisieren eines Moduls.
     * 
     * @param id           Die ID des zu aktualisierenden Moduls.
     * @param updatedModul Das aktualisierte Modul.
     * @return Eine ResponseEntity, die das aktualisierte Modul und den
     *         HTTP-Statuscode enthält.
     * @throws ResponseStatusException Falls das Modul nicht gefunden wird oder der
     *                                 Benutzer nicht autorisiert ist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Modul> updateModul(@PathVariable String id, @RequestBody Modul updatedModul) {
        // Vorhandenes Modul anhand der ID abrufen
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

        Optional<Modul> existingModul = modulService.getModul(Long.parseLong(id));

        // Überprüfen, ob das Modul vorhanden ist
        if (existingModul.isPresent()) {
            // Wenn das Modul vorhanden ist, das Modul-Objekt aus dem Optional
            // extrahieren
            Modul modul = existingModul.get();

            Fachgruppe fachgruppe = fachgruppeService
                    .getFachgruppe(modul.getFachgruppeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachgruppe nicht gefunden"));

            // Überprüfe, ob der Benutzer Fachbereichsreferent/Stellvertreter,
            // Fachgruppenreferent/Stellvertreter, Modulbeauftrager oder Admin ist.
            if (!(fachgruppe.getReferent().getId() == nutzer.getId()
                    || fachgruppe.getStellvertreter().getId() == nutzer.getId()
                    || fachgruppe.getFachbereich().getReferent().getId() == nutzer.getId()
                    || fachgruppe.getFachbereich().getStellvertreter().getId() == nutzer.getId()
                    || modul.getModulbeauftragter().getId() == nutzer.getId()
                    || nutzer.getRole().equals("ADMIN"))) {
                // Falls der Benutzer nicht der Fachbereichsreferent/Stellvertreter,
                // Fachgruppenreferent/Stellvertreter, Modulbeauftrager des Moduls ist oder kein
                // Administrator ist, einen 401 Fehler zurückgeben
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
            }

            Mitarbeiter modulbeauftragter = mitarbeiterService
                    .getMitarbeiter(updatedModul.getModulbeauftragterId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modulbeauftragter not found"));

            Modulgruppe modulgruppe = modulgruppeService
                    .getModulgruppe(updatedModul.getModulgruppeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modulgruppe not found"));

            modul.setWorkload(updatedModul.getWorkload());
            modul.setCredits(updatedModul.getCredits());
            modul.setDauer(updatedModul.getDauer());
            modul.setBeschreibung(updatedModul.getBeschreibung());
            modul.setSprache(updatedModul.getSprache());
            modul.setFreigegeben(updatedModul.isFreigegeben());
            modul.setLehrveranstaltungsort(updatedModul.getLehrveranstaltungsort());
            modul.setModulbeauftragter(modulbeauftragter);
            modul.setModulgruppe(modulgruppe);

            // Validierungslogik für die Eingabefelder
            List<String> errors = validateModul(updatedModul);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            modulService.saveAndFlush(modul);

            return new ResponseEntity<>(modul, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden");
        }
    }

    /**
     * Validiert das übergebene Modul-Objekt.
     * 
     * @param modul das zu validierende Modul-Objekt
     * @return eine Liste von Fehlermeldungen, leer wenn keine Validierungsfehler
     *         vorliegen
     */
    List<String> validateModul(Modul modul) {
        List<String> errors = new ArrayList<>();

        // Überprüfung erforderlicher Felder
        // Namesprüfung
        if (modul.getName() == null || modul.getName().isEmpty()) {
            errors.add("Das Feld 'Name' ist erforderlich.");
        }

        // Längenprüfung
        if (modul.getName() != null && modul.getName().length() < 2) {
            errors.add("Das Feld 'Name' muss mindestens 2 Zeichen lang sein.");
        }

        // Überprüfen, ob Workload vorhanden ist
        if (modul.getWorkload() <= 0) {
            errors.add("Das Feld 'Workload' ist erforderlich.");
        }

        // Überprüfen, ob Credits vorhanden sind
        if (modul.getCredits() <= 0) {
            errors.add("Das Feld 'Credits' ist erforderlich.");
        }

        // Überprüfen, ob Dauer vorhanden ist
        if (modul.getDauer() <= 0) {
            errors.add("Das Feld 'Dauer' ist erforderlich.");
        }

        // Überprüfen, ob Art ausgewählt ist
        if (modul.getArt() == null) {
            errors.add("Das Feld 'Art' ist erforderlich.");
        }

        // Überprüfen, ob Abschluss ausgewählt ist
        if (modul.getAbschluss() == null) {
            errors.add("Das Feld 'Abschluss' ist erforderlich.");
        }

        // Überprüfen, ob Beschreibung vorhanden ist
        if (modul.getBeschreibung() == null || modul.getBeschreibung().isEmpty()) {
            errors.add("Das Feld 'Beschreibung' ist erforderlich.");
        }

        // Überprüfen, ob Lehrveranstaltungsort vorhanden ist
        if (modul.getLehrveranstaltungsort() == null || modul.getLehrveranstaltungsort().isEmpty()) {
            errors.add("Das Feld 'Lehrveranstaltungsort' ist erforderlich.");
        }

        // Überprüfen, ob Sprache ausgewählt ist
        if (modul.getSprache() == null) {
            errors.add("Das Feld 'Sprache' ist erforderlich.");
        }

        if (modul.getFachgruppe() == null) {
            errors.add("Fachgruppe ist nicht gesetzt.");
        }

        if (modul.getModulbeauftragter() == null) {
            errors.add("Modulbeauftrager ist nicht gesetzt");
        }

        if (modul.getModulgruppe() == null) {
            errors.add("Modulgruppe ist nicht gesetzt");
        }

        return errors;
    }

}
