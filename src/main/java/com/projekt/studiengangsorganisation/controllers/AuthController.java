package com.projekt.studiengangsorganisation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.AdminService;
import com.projekt.studiengangsorganisation.service.NutzerService;

/**
 * Controller-Klasse für die Authentifizierung.
 * 
 * @author Paul Rakow
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    // PasswordEncoder zum Verschlüsseln von Passwörtern
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Deklarierung Services
    @Autowired
    private NutzerService nutzerService;

    // Service für Adminoperationen
    @Autowired
    private AdminService adminService;

    /**
     * Methode zum Registrieren eines Admin-Benutzers.
     * 
     * @param user Der zu registrierende Admin-Benutzer.
     * @return Eine Bestätigungsnachricht.
     */
    @PostMapping("/register/admin")
    public String registerUser(@RequestBody Admin user) {
        // Verschlüssele das Passwort und setze den Benutzernamen
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getVorname().toLowerCase() + "." + user.getNachname().toLowerCase());
        // Speichere den Benutzer in der Datenbank
        adminService.saveAndFlush(user);
        return "Benutzer erfolgreich registriert";
    }

    /**
     * Methode zum Abrufen von Informationen über den angemeldeten Benutzer.
     * 
     * @return Informationen über den angemeldeten Benutzer.
     */
    @GetMapping("/info")
    public Nutzer getUserInfo() {
        // Erhalte die Authentifizierungsinformationen des aktuellen Benutzers
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Suche den Nutzer anhand des Benutzernamens
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName()).get();
        return nutzer;
    }
}