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

import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für die Verwaltung von Modulgruppen.
 */
@RequestMapping("/modulgruppe")
@RestController
public class ModulgruppeController {

    // Deklarierung Services
    @Autowired
    ModulgruppeService modulgruppeService;

    @Autowired
    NutzerService nutzerService;

    /**
     * Methode zum Abrufen einer Modulgruppe anhand ihrer ID.
     * @param id Die ID der Modulgruppe.
     * @return Die gefundene Modulgruppe.
     * @throws ResponseStatusException Falls die Modulgruppe nicht gefunden wird.
     */
    @GetMapping("/{id}")
    public Modulgruppe getOne(@PathVariable String id) {
        // Versuche, die Modulgruppe mit der angegebenen ID zu erhalten
        Optional<Modulgruppe> modulgruppe = modulgruppeService.getModulgruppe(Long.parseLong(id));

        // Wenn die Modulgruppe gefunden wurde
        if (modulgruppe.isPresent()) {
            return modulgruppe.get();
        } else {
            // Andernfalls wirf einen Fehler 404 - Ressource nicht gefunden
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Methode zum Abrufen aller Modulgruppen.
     * @param response Der HTTP-Response.
     * @return Eine Liste aller Modulgruppen.
     */
    @GetMapping("")
    public List<Modulgruppe> getAll(HttpServletResponse response) {
        // Erhalte eine Liste aller Modulgruppen
        List<Modulgruppe> list = modulgruppeService.getModulgruppen();
        // Setze den Content-Range Header im Response, um die Anzahl der Modulgruppen anzugeben
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    /**
     * Methode zum Erstellen einer neuen Modulgruppe.
     * @param modulgruppe Die zu erstellende Modulgruppe.
     * @return Eine ResponseEntity, die die erstellte Modulgruppe und den HTTP-Statuscode enthält.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist.
     */
    @PostMapping("")
    public ResponseEntity<Modulgruppe> createModulgruppe(@RequestBody Modulgruppe modulgruppe) {
        // Überprüfe, ob der Benutzer autorisiert ist
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert"));

        // Erlaubt sind MITARBEITER und ADMIN
        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            // Wenn der Benutzer nicht autorisiert ist, wirf einen Fehler 401 - Nicht autorisiert
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Benutzer nicht autorisiert");
        }

        // Speichere die Modulgruppe in der Datenbank
        modulgruppeService.saveAndFlush(modulgruppe);

        // Gib eine Erfolgsantwort zurück
        return new ResponseEntity<>(modulgruppe, HttpStatus.CREATED);
    }
}