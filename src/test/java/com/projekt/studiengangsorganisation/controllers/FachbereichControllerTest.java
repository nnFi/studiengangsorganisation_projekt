package com.projekt.studiengangsorganisation.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den FachbereichController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 */
public class FachbereichControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
    @Mock
    private FachbereichService fachbereichService;

    @Mock
    private NutzerService nutzerService;

    @Mock
    private MitarbeiterService mitarbeiterService;

    @InjectMocks
    private FachbereichController controller;

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
        MockitoAnnotations.initMocks(this); // Initialisierung der Mocks
        SecurityContextHolder.setContext(securityContext); // Setzen des SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication); // Mocken der Authentifizierung
    }

    /**
     * Testet die Methode getOne mit einer gültigen ID.
     * Erwartet, dass der Fachbereich zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsFachbereich() {
        // Mocken eines gültigen Fachbereichs mit Referent und Stellvertreter
        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(101L);
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(102L);
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);

        // Mocken des Service-Aufrufs für getFachbereich
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));

        // Aufruf der Controller-Methode
        Fachbereich result = controller.getOne("1");

        // Assertions
        assertEquals(fachbereich.getId(), result.getId());
        assertEquals(referent.getId(), result.getReferentId());
        assertEquals(stellvertreter.getId(), result.getStellvertreterId());
    }

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken eines leeren Optional für ungültige ID
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.empty());

        // Überprüfung, ob die Controller-Methode eine ResponseStatusException wirft
        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode getAll.
     * Erwartet, dass alle Fachbereiche zurückgegeben werden.
     * 
     * @return void
     */
    @Test
    public void testGetAll_ReturnsAllFachbereiche() {
        // Mocken von Fachbereich-Objekten mit Referenten und Stellvertretern
        Mitarbeiter referent1 = new Mitarbeiter();
        referent1.setId(101L);
        Mitarbeiter stellvertreter1 = new Mitarbeiter();
        stellvertreter1.setId(102L);
        Fachbereich fachbereich1 = new Fachbereich();
        fachbereich1.setId(1L);
        fachbereich1.setReferent(referent1);
        fachbereich1.setStellvertreter(stellvertreter1);

        Mitarbeiter referent2 = new Mitarbeiter();
        referent2.setId(201L);
        Mitarbeiter stellvertreter2 = new Mitarbeiter();
        stellvertreter2.setId(202L);
        Fachbereich fachbereich2 = new Fachbereich();
        fachbereich2.setId(2L);
        fachbereich2.setReferent(referent2);
        fachbereich2.setStellvertreter(stellvertreter2);

        List<Fachbereich> fachbereiche = Arrays.asList(fachbereich1, fachbereich2);

        // Mocken des Service-Aufrufs für getFachbereiche
        when(fachbereichService.getFachbereiche()).thenReturn(fachbereiche);

        // Aufruf der Controller-Methode
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Fachbereich> result = controller.getAll(response);

        // Assertion
        assertEquals(2, result.size());
    }

    /**
     * Testet die Methode createFachbereich durch einen Administrator.
     * Erwartet, dass der Fachbereich erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateFachbereich_Administrator_SuccessfullyCreated() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken eines zu erstellenden Fachbereichs
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName("Test Fachbereich");
        fachbereich.setReferentId(1L);
        fachbereich.setStellvertreterId(2L);

        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(1L);
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(referent));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(stellvertreter));

        when(fachbereichService.saveAndFlush(any(Fachbereich.class))).thenReturn(fachbereich);

        // Aufruf der Controller-Methode
        ResponseEntity<Fachbereich> response = controller.createFachbereich(fachbereich);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(fachbereich, response.getBody());
    }

    /**
     * Testet die Methode createFachbereich durch einen Studenten.
     * Erwartet, dass der Fachbereich nicht erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateFachbereich_Student_Unauthorized() {
        // Mocken eines Student-Nutzers
        Nutzer student = new Student();
        student.setUsername("test.student");
        student.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.student");
        when(nutzerService.getNutzerByUsername("test.student")).thenReturn(Optional.of(student));

        // Mocken eines zu erstellenden Fachbereichs
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName("Test Fachbereich");
        fachbereich.setReferentId(1L);
        fachbereich.setStellvertreterId(2L);

        // Erwartung einer ResponseStatusException
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.createFachbereich(fachbereich);
        });

        // Überprüfung, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Testet die Methode updateFachbreich durch einen Administrator.
     * Erwartet, dass der Fachbereich erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @SuppressWarnings("null")
    @Test
    public void testUpdateFachbereich_Administrator_SuccessfullyUpdated() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken eines vorhandenen Fachbereichs
        Fachbereich existingFachbereich = new Fachbereich();
        existingFachbereich.setId(1L);
        existingFachbereich.setName("Alter Fachbereich");
        existingFachbereich.setReferentId(1L); // Beispiel-ID
        existingFachbereich.setStellvertreterId(2L); // Beispiel-ID

        // Mocken eines aktualisierten Fachbereichs mit korrekten IDs
        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(2L); // Beispiel-ID
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(3L); // Beispiel-ID

        Fachbereich updatedFachbereich = new Fachbereich();
        updatedFachbereich.setId(1L);
        updatedFachbereich.setName("Neuer Fachbereich");
        updatedFachbereich.setReferentId(referent.getId()); // Beispiel-ID
        updatedFachbereich.setStellvertreterId(stellvertreter.getId()); // Beispiel-ID

        // Mocken der Service-Aufrufe
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(existingFachbereich));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(referent));
        when(mitarbeiterService.getMitarbeiter(3L)).thenReturn(Optional.of(stellvertreter));
        
        // Aufruf der Controller-Methode
        ResponseEntity<Fachbereich> response = controller.updateFachbereich(1L, updatedFachbereich);

        // Manueller Vergleich der Attribute
        assertEquals(updatedFachbereich.getReferentId(), response.getBody().getReferent().getId());
        assertEquals(updatedFachbereich.getStellvertreterId(), response.getBody().getStellvertreter().getId());
    }

    /**
     * Testet die Methode updateFachbreich durch einen nicht-Admin-Nutzer.
     * Erwartet, dass der Fachbereich nicht aktualisiert wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateFachbereich_NotAuthorized_ThrowsResponseStatusException() {
        // Mocken eines Nicht-Admin-Nutzers
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Mocken eines vorhandenen Fachbereichs
        Fachbereich existingFachbereich = new Fachbereich();
        existingFachbereich.setId(1L);
        existingFachbereich.setName("Alter Fachbereich");
        existingFachbereich.setReferentId(1L);
        existingFachbereich.setStellvertreterId(2L);

        // Erwartung einer ResponseStatusException
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.updateFachbereich(1L, existingFachbereich);
        });

        // Überprüfung, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Testet die Validierung eines Fachbereichs mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     * 
     * @return void
     */
    @Test
    public void testValidateFachbereich_ValidInputs() {
        // Vorbereitung eines gültigen Fachbereichs
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName("Testfachbereich");
        fachbereich.setReferentId(1L);
        fachbereich.setStellvertreterId(2L);

        // Ausführen der Validierung
        List<String> errors = controller.validateFachbereich(fachbereich);

        // Überprüfen, ob keine Fehler in der Fehlerliste enthalten sind
        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    /**
     * Testet die Validierung eines Fachbereichs mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param name  der Name des Fachbereichs
     * @param referentId die Id des Referenten
     * @param stellvertreterId die Id des Stellvertreters
     */
    @ParameterizedTest
    @CsvSource({
            ", 1, 2",
            "T, 1, 2",
            "Testfachbereich, , 2",
            "Testfachbereich, 1, "
    })
    public void testValidateFachbereich_InvalidInputs(String name, String referentId, String stellvertreterId) {
        // Vorbereitung eines Fachbereichs mit den gegebenen Eingaben
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName(name);
        fachbereich.setReferentId(referentId == null ? null : Long.parseLong(referentId));
        fachbereich.setStellvertreterId(stellvertreterId == null ? null : Long.parseLong(stellvertreterId));

        // Ausführen der Validierung
        List<String> errors = controller.validateFachbereich(fachbereich);

        // Überprüfen, ob Fehler in der Fehlerliste enthalten sind
        assertTrue(!errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}