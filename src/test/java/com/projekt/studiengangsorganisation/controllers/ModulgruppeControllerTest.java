package com.projekt.studiengangsorganisation.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den FachbereichController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 */
public class ModulgruppeControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
    @Mock
    private ModulgruppeService modulgruppeService;

    @Mock
    private NutzerService nutzerService;

    @InjectMocks
    private ModulgruppeController controller;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this); // Initialisierung der Mocks
        SecurityContextHolder.setContext(securityContext); // Setzen des SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication); // Mocken der Authentifizierung
    }

    /**
     * Testet die Methode getOne mit einer gültigen ID.
     * Erwartet, dass die Modulgruppe zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsModulgruppe() {
        // Mocken einer Modulgruppe mit gültiger ID
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setId(1L);
        modulgruppe.setName("Test Modulgruppe");

        // Wenn der Service aufgerufen wird, geben Sie die modellierte Modulgruppe zurück
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.of(modulgruppe));

        // Aufruf der Controller-Methode, um die Modulgruppe mit ID "1" zu erhalten
        Modulgruppe result = controller.getOne("1");

        // Assertions
        assertEquals(modulgruppe.getId(), result.getId(), "Die ID der Modulgruppe stimmt nicht überein.");
        assertEquals(modulgruppe.getName(), result.getName(), "Der Name der Modulgruppe stimmt nicht überein.");
    }
    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass keine Modulgruppe zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken eines leeren Optional für ungültige Modulgruppen-ID
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.empty());

        // Erwartung, dass eine ResponseStatusException geworfen wird
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });

        // Überprüfen, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    /**
     * Testet die Methode getAll.
     * Erwartet, dass alle Modulgruppen zurückgegeben werden.
     * 
     * @return void
     */
    @Test
    public void testGetAll_ReturnsAllModulgruppen() {
        // Vorbereitung von zwei Modulgruppen
        Modulgruppe modulgruppe1 = new Modulgruppe();
        modulgruppe1.setId(1L);
        modulgruppe1.setName("Modulgruppe 1");

        Modulgruppe modulgruppe2 = new Modulgruppe();
        modulgruppe2.setId(2L);
        modulgruppe2.setName("Modulgruppe 2");

        // Erzeugen einer Liste von Modulgruppen
        List<Modulgruppe> modulgruppen = Arrays.asList(modulgruppe1, modulgruppe2);

        // Mocken des Service-Aufrufs für getModulgruppen
        when(modulgruppeService.getModulgruppen()).thenReturn(modulgruppen);

        // Mocken des HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Aufruf der Controller-Methode zur Rückgabe aller Modulgruppen
        List<Modulgruppe> result = controller.getAll(response);

        // Assertions
        assertEquals(2, result.size()); // Überprüfen, ob zwei Modulgruppen zurückgegeben wurden
    }

    /**
     * Testet die Methode createModulgruppe durch einen Administrator.
     * Erwartet, dass die Modulgruppe erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateModulgruppe_Administrator_SuccessfullyCreated() {
        // Mocken eines Administrator-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Konfigurieren der Mocks für Authentifizierung und Nutzerabfrage
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Vorbereitung einer zu erstellenden Modulgruppe
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Test Modulgruppe");

        // Mocken des Service-Aufrufs für das Speichern und Flushen der Modulgruppe
        when(modulgruppeService.saveAndFlush(any(Modulgruppe.class))).thenReturn(modulgruppe);

        // Aufruf der Controller-Methode zur Erstellung der Modulgruppe
        ResponseEntity<Modulgruppe> response = controller.createModulgruppe(modulgruppe);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Überprüfen des Statuscodes 201 CREATED
        assertEquals(modulgruppe, response.getBody()); // Überprüfen, ob die zurückgegebene Modulgruppe korrekt ist
    }

    /**
     * Testet die Methode createModulgruppe durch einen Studenten.
     * Erwartet, dass die Modulgruppe nicht erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateModulgruppe_NotAuthorized_ThrowsResponseStatusException() {
        // Mocken eines nicht autorisierten Nutzers (Studentenrolle)
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        // Konfigurieren der Mocks für Authentifizierung und Nutzerabfrage
        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Vorbereitung einer zu erstellenden Modulgruppe
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Test Modulgruppe");

        // Mocken des Service-Aufrufs für das Speichern und Flushen der Modulgruppe
        when(modulgruppeService.saveAndFlush(any(Modulgruppe.class))).thenReturn(modulgruppe);

        // Erwartung einer ResponseStatusException, wenn die Methode aufgerufen wird
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.createModulgruppe(modulgruppe);
        });

        // Überprüfen, ob der Statuscode der Exception UNAUTHORIZED ist
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Testet die Validierung einer Modulgruppe mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     * 
     * @return void
     */
    @Test
    public void testValidateModulgruppe_ValidInputs() {
        // Vorbereitung einer Modulgruppe mit gültigem Namen
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Testmodulgruppe");

        // Ausführen der Validierung
        List<String> errors = controller.validateModulgruppe(modulgruppe);

        // Überprüfen, ob keine Fehler in der Fehlerliste enthalten sind
        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    /**
     * Testet die Validierung einer Modulgruppe mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param name  der Name der Modulgruppe
     */
    @ParameterizedTest
    @CsvSource({
            ",",
            "T"
    })
    public void testValidateModulgruppe_InvalidInputs(String name) {
        // Vorbereitung einer Modulgruppe mit ungültigem Namen
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName(name);

        // Ausführen der Validierung
        List<String> errors = controller.validateModulgruppe(modulgruppe);

        // Überprüfen, ob Fehler in der Fehlerliste enthalten sind
        assertFalse(errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}