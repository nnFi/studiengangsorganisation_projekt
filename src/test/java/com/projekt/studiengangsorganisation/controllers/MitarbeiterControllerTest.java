package com.projekt.studiengangsorganisation.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

/**
 * Testklasse für den MitarbeiterController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden zu testen.
 */
public class MitarbeiterControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft Interaktionen
    @Mock
    private MitarbeiterService mitarbeiterService;

    @Mock
    private NutzerService nutzerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MitarbeiterController controller;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    /**
     * Setzt die Testumgebung auf.
     * Initialisiert die Mock-Objekte und setzt den Sicherheitskontext.
     * 
     * @return void
     */
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        // Initialisiert die Mock-Objekte
        MockitoAnnotations.initMocks(this);

        // Setzt den Sicherheitskontext für die Tests
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * Testet die Methode getOne mit einer gültigen ID.
     * Erwartet, dass der Mitarbeiter zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsMitarbeiter() {
        // Test für das Abrufen eines Mitarbeiters mit gültiger ID
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setId(1L);
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(mockMitarbeiter));

        Mitarbeiter result = controller.getOne("1");

        assertEquals(mockMitarbeiter, result);
    }

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Test für das Abrufen eines Mitarbeiters mit ungültiger ID
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode createMitarbeiter durch einen Administrator.
     * Erwartet, dass der Mitarbeiter erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateMitarbeiter_Administrator_SuccessfullyCreated() {        
        // Test für das Erstellen eines Mitarbeiters durch einen Administrator
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Setzt den Sicherheitskontext, um den Administrator-Benutzer zurückzugeben
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocking des Mitarbeiters
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setVorname("Muster");
        mockMitarbeiter.setNachname("Mitarbeiter");
        mockMitarbeiter.setPassword("Secure!Pass123");

        // Mocking der Passwortkodierung
        when(passwordEncoder.encode(mockMitarbeiter.getPassword())).thenReturn("Secure!Pass123");

        // Mocking der saveAndFlush-Methode
        when(mitarbeiterService.saveAndFlush(any(Mitarbeiter.class))).thenReturn(mockMitarbeiter);

        // Ausführen des createMitarbeiter-Aufrufs
        ResponseEntity<Mitarbeiter> response = controller.createMitarbeiter(mockMitarbeiter);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Testet die Methode createMitarbeiter durch einen Nicht-Administrator.
     * Erwartet, dass der Mitarbeiter nicht erstellt wird und eine Ausnahme ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testCreateMitarbeiter_NonAdministrator_NotCreated() {
        // Test für das Erstellen eines Mitarbeiters durch einen Nicht-Administrator
        Nutzer user = new Mitarbeiter();
        user.setUsername("mia.mitarbeiter1");
        user.setRole("MITARBEITER");

        // Setzt den Sicherheitskontext, um den Nicht-Administrator-Benutzer zurückzugeben
        when(authentication.getName()).thenReturn("mia.mitarbeiter1");
        when(nutzerService.getNutzerByUsername("mia.mitarbeiter1")).thenReturn(Optional.of(user));

        // Mocking des Mitarbeiters
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setVorname("Muster");
        mockMitarbeiter.setNachname("Mitarbeiter");
        mockMitarbeiter.setPassword("Secure!Pass123");

        // Ausführen des createMitarbeiter-Aufrufs und Erwarten einer Ausnahme
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createMitarbeiter(mockMitarbeiter);
        });

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    /**
     * Testet die Methode updateMitarbeiter durch einen Administrator.
     * Erwartet, dass der Mitarbeiter erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @SuppressWarnings("null")
    @Test
    public void testUpdateMitarbeiter_Administrator_SuccessfullyUpdated() {
        // Test für das Aktualisieren eines Mitarbeiters durch einen Administrator
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Setzt den Sicherheitskontext, um den Administrator-Benutzer zurückzugeben
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocking des existierenden Mitarbeiters
        Mitarbeiter existingMitarbeiter = new Mitarbeiter();
        existingMitarbeiter.setId(1L);
        existingMitarbeiter.setVorname("Muster");
        existingMitarbeiter.setNachname("Mitarbeiter");

        // Mocking des aktualisierten Mitarbeiters
        Mitarbeiter updatedMitarbeiter = new Mitarbeiter();
        updatedMitarbeiter.setVorname("Musterin");
        updatedMitarbeiter.setNachname("Mitarbeiterin");
        updatedMitarbeiter.setPassword("Secure!Pass123");

        // Mocking der getMitarbeiter-Methode
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(existingMitarbeiter));

        // Mocking der saveAndFlush-Methode
        when(mitarbeiterService.saveAndFlush(any(Mitarbeiter.class))).thenReturn(existingMitarbeiter);

        // Ausführen des updateMitarbeiter-Aufrufs
        ResponseEntity<Mitarbeiter> response = controller.updateMitarbeiter("1", updatedMitarbeiter);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Musterin", response.getBody().getVorname());
        assertEquals("Mitarbeiterin", response.getBody().getNachname());
    }

    /**
     * Testet die Methode updateMitarbeiter durch einen Nicht-Administrator.
     * Erwartet, dass der Mitarbeiter nicht aktualisiert wird und eine Ausnahme ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateMitarbeiter_NonAdministrator_NotUpdated() {
        // Test für das Aktualisieren eines Mitarbeiters durch einen Nicht-Administrator
        Nutzer user = new Mitarbeiter();
        user.setUsername("mia.mitarbeiter1");
        user.setRole("MITARBEITER");

        // Setzt den Sicherheitskontext, um den Nicht-Administrator-Benutzer zurückzugeben
        when(authentication.getName()).thenReturn("mia.mitarbeiter1");
        when(nutzerService.getNutzerByUsername("mia.mitarbeiter1")).thenReturn(Optional.of(user));

        // Mocking des existierenden Mitarbeiters
        Mitarbeiter existingMitarbeiter = new Mitarbeiter();
        existingMitarbeiter.setId(1L);
        existingMitarbeiter.setVorname("Muster");
        existingMitarbeiter.setNachname("Mitarbeiter");

        // Mocking des aktualisierten Mitarbeiters
        Mitarbeiter updatedMitarbeiter = new Mitarbeiter();
        updatedMitarbeiter.setVorname("Musterin");
        updatedMitarbeiter.setNachname("Mitarbeiterin");
        updatedMitarbeiter.setPassword("Secure!Pass123");

        // Mocking der getMitarbeiter-Methode
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(existingMitarbeiter));

        // Ausführen des updateMitarbeiter-Aufrufs und Erwarten einer Ausnahme
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateMitarbeiter("1", updatedMitarbeiter);
        });

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    /**
     * Testet die Validierung eines Mitarbeiters mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     *
     * @param vorname   der Vorname des Mitarbeiters
     * @param nachname  der Nachname des Mitarbeiters
     * @param password  das Passwort des Mitarbeiters
     */
    @ParameterizedTest
    @CsvSource({
            "Johann, Schneider, Valid1!password",
            "Alicia, Schmidt, AnotherValid1Passwo-rd",
            "Emilia, Becker, Secure!Pass123",
    })
    public void testValidateMitarbeiter_ValidInputs(String vorname, String nachname, String password) {
        // Test für die Validierung eines Mitarbeiters mit gültigen Eingaben
        vorname = vorname != null ? vorname : "";
        nachname = nachname != null ? nachname : "";
        password = password != null ? password : "";

        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname(vorname);
        mitarbeiter.setNachname(nachname);
        mitarbeiter.setPassword(password);

        List<String> errors = controller.validateMitarbeiter(mitarbeiter);

        assertTrue(errors.isEmpty(), "Es sollte keine Validierungsfehler auftreten.");
    }

    /**
     * Testet die Validierung eines Mitarbeiters mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param vorname   der Vorname des Mitarbeiters
     * @param nachname  der Nachname des Mitarbeiters
     * @param password  das Passwort des Mitarbeiters
     */
    @ParameterizedTest
    @CsvSource({
            ", Schmidt, Valid1!password", // Leerzeichen als Vorname
            "Johann, , Valid1!password", // Leerzeichen als Nachname
            ", , Valid1!password", // Leerzeichen als Vor- und Nachname
            "Johann, Schmidt, ", // Leerzeichen als Passwort
            "Johann, Schmidt, 123", // Passwort zu kurz
            "Johann, Schmidt, invalid", // Passwort enthält kein Sonderzeichen
            "Johann, Schmidt, invalidpassword", // Passwort enthält kein Großbuchstaben
            "Johann, Schmidt, invalidPassword", // Passwort enthält keine Zahl
            "Johann, Schmidt, INVALID!", // Passwort enthält kein Kleinbuchstaben
    })
    public void testValidateMitarbeiter_InvalidInputs(String vorname, String nachname, String password) {
        // Test für die Validierung eines Mitarbeiters mit ungültigen Eingaben
        vorname = vorname != null ? vorname : "";
        nachname = nachname != null ? nachname : "";
        password = password != null ? password : "";

        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname(vorname);
        mitarbeiter.setNachname(nachname);
        mitarbeiter.setPassword(password);

        List<String> errors = controller.validateMitarbeiter(mitarbeiter);

        assertTrue(!errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}