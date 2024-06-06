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

import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
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
     * Methode zum Abrufen aller Module.
     * 
     * @param response Der HTTP-Response.
     * @return Eine Liste aller Module.
     */
    @GetMapping("")
    public List<Modul> getAll(HttpServletResponse response) {
        // Erhalte eine Liste aller Module
        List<Modul> list = modulService.getModule();

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

        // Überprüfe, ob der Benutzer berechtigt ist, das Modul für die Fachgruppe zu
        // erstellen
        Fachgruppe fachgruppe = fachgruppeService
                .getFachgruppe(modul.getFachgruppeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fachgruppe nicht gefunden"));

        if (!fachgruppe.getReferent().getId().equals(nutzer.getId())
                && !fachgruppe.getStellvertreter().getId().equals(nutzer.getId())
                && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Validierungslogik für die Eingabefelder
        /*List<String> errors = validateModul (modul);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }*/

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

        //Überprüfen, ob das Modul vorhanden ist
        if (existingModul.isPresent()) {
            // Wenn das Modul vorhanden ist, das Modul-Objekt aus dem Optional
            // extrahieren
            Modul modul = existingModul.get();

            // Überprüfe, ob der Benutzer ein Modulbeauftrager oder Admin ist
            if (!(modul.getModulbeauftragter().getId() == nutzer.getId()
                    || nutzer.getRole().equals("ADMIN"))) {
                // Falls der Benutzer nicht der Modulbeauftrager des Moduls ist oder kein 
                //Administrator ist, einen 401 Fehler zurückgeben
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
            }

            // Validierungslogik für die Eingabefelder
            /*List<String> errors = validateModul(updatedModul);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }*/

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
            modulService.saveAndFlush(modul);

            return new ResponseEntity<>(modul, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden");
        }
    }

    
}
