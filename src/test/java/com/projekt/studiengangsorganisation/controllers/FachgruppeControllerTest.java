package com.projekt.studiengangsorganisation.controllers;

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
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Testklasse für den FachgruppeController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 * 
 * @author Bao Chau Kathi Doan
 */
public class FachgruppeControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
    @Mock
    private FachgruppeService fachgruppeService;

    @Mock
    private NutzerService nutzerService;

    @Mock
    private MitarbeiterService mitarbeiterService;

    @Mock
    private FachbereichService fachbereichService;

    @InjectMocks
    private FachgruppeController controller;

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
     * Erwartet, dass die Fachgruppe zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsFachgruppe() {
        // Mocken einer gültigen Fachgruppe mit Referent, Stellvertreter und Fachbereich
        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(101L);
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(102L);
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(50L);
        fachbereich.setName("Informatik");

        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setId(1L);
        fachgruppe.setReferent(referent);
        fachgruppe.setStellvertreter(stellvertreter);
        fachgruppe.setFachbereich(fachbereich);

        // Mocken des Service-Aufrufs für getFachgruppe
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.of(fachgruppe));

        // Aufruf der Controller-Methode
        Fachgruppe result = controller.getOne("1");

        // Assertions
        assertEquals(fachgruppe, result);
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
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.empty());

        // Erwartung einer ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    @Test
    public void testGetAll_ReturnsAllFachgruppen() {
        // Mocken von zwei Fachbereichen
        Fachbereich fachbereich1 = new Fachbereich();
        fachbereich1.setId(1L);
        fachbereich1.setName("Informatik");

        Fachbereich fachbereich2 = new Fachbereich();
        fachbereich2.setId(2L);
        fachbereich2.setName("Physik");

        // Mocken von zwei Fachgruppen mit korrekten Referenten, Stellvertretern und
        // Fachbereichen
        Mitarbeiter referent1 = new Mitarbeiter();
        referent1.setId(101L);
        Mitarbeiter stellvertreter1 = new Mitarbeiter();
        stellvertreter1.setId(102L);

        Mitarbeiter referent2 = new Mitarbeiter();
        referent2.setId(201L);
        Mitarbeiter stellvertreter2 = new Mitarbeiter();
        stellvertreter2.setId(202L);

        Fachgruppe fachgruppe1 = new Fachgruppe();
        fachgruppe1.setId(1L);
        fachgruppe1.setName("Test Fachgruppe 1");
        fachgruppe1.setFachbereich(fachbereich1);
        fachgruppe1.setReferent(referent1);
        fachgruppe1.setStellvertreter(stellvertreter1);

        Fachgruppe fachgruppe2 = new Fachgruppe();
        fachgruppe2.setId(2L);
        fachgruppe2.setName("Test Fachgruppe 2");
        fachgruppe2.setFachbereich(fachbereich2);
        fachgruppe2.setReferent(referent2);
        fachgruppe2.setStellvertreter(stellvertreter2);

        List<Fachgruppe> fachgruppenList = Arrays.asList(fachgruppe1, fachgruppe2);

        // Mocken des Service-Aufrufs für getAllFachgruppen
        when(fachgruppeService.getFachgruppen()).thenReturn(fachgruppenList);

        // Aufruf der Controller-Methode
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Fachgruppe> result = controller.getAll(response);

        // Assertion
        assertEquals(2, result.size());
    }

    /**
     * Testet die Methode createFachbereich durch einen Administrator.
     * Erwartet, dass die Fachgruppe erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateFachgruppe_Administrator_SuccessfullyCreated() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken eines zu erstellenden Fachgruppe
        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setName("Test Fachgruppe");
        fachgruppe.setKuerzel("TFG");
        fachgruppe.setFachbereichId(1L);
        fachgruppe.setReferentId(2L);
        fachgruppe.setStellvertreterId(3L);

        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(2L);
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(3L);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setName("Informatik");
        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);

        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(referent));
        when(mitarbeiterService.getMitarbeiter(3L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));

        // Aufruf der Controller-Methode
        ResponseEntity<Fachgruppe> response = controller.createFachgruppe(fachgruppe);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Testet die Methode updateFachgruppe durch einen Administrator.
     * Erwartet, dass die Fachgruppe erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @SuppressWarnings("null")
    @Test
    public void testUpdateFachgruppe_Administrator_SuccessfullyUpdated() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken eines Fachbereichs
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(50L);
        fachbereich.setName("Informatik");

        // Mocken eines vorhandenen Fachgruppe
        Fachgruppe existingFachgruppe = new Fachgruppe();
        existingFachgruppe.setId(1L);
        existingFachgruppe.setName("Informatik");
        existingFachgruppe.setReferentId(1L);
        existingFachgruppe.setStellvertreterId(2L);
        existingFachgruppe.setFachbereich(fachbereich);

        // Mocken einer aktualisierten Fachgruppe mit korrekten IDs und gültigem Kürzel
        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(2L);
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(3L);

        Fachgruppe updatedFachgruppe = new Fachgruppe();
        updatedFachgruppe.setId(1L);
        updatedFachgruppe.setName("Informatik");
        updatedFachgruppe.setKuerzel("INF");
        updatedFachgruppe.setReferentId(referent.getId());
        updatedFachgruppe.setStellvertreterId(stellvertreter.getId());
        updatedFachgruppe.setFachbereichId(50L);

        // Mocken der Service-Aufrufe
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.of(existingFachgruppe));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(referent));
        when(mitarbeiterService.getMitarbeiter(3L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(50L)).thenReturn(Optional.of(fachbereich));

        // Aufruf der Controller-Methode
        ResponseEntity<Fachgruppe> response = controller.updateFachgruppe("1", updatedFachgruppe);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFachgruppe.getReferentId(), response.getBody().getReferent().getId());
        assertEquals(updatedFachgruppe.getStellvertreterId(), response.getBody().getStellvertreter().getId());
    }

    /**
     * Testet die Methode createFachgruppe durch einen Studenten.
     * Erwartet, dass die Fachgruppe nicht erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateFachgruppe_NotAuthorized_ThrowsResponseStatusException() {
        // Mocken eines Nicht-Admin-Nutzers
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Mocken einer neuen Fachgruppe, die erstellt werden soll
        Fachgruppe newFachgruppe = new Fachgruppe();
        newFachgruppe.setName("Neue Fachgruppe");

        // Erwartung einer ResponseStatusException
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.createFachgruppe(newFachgruppe);
        });

        // Überprüfung, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Testet die Methode updateFachgruppe durch einen nicht-Admin-Nutzer.
     * Erwartet, dass die Fachgruppe erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateFachgruppe_NotAuthorized_ThrowsResponseStatusException() {
        // Mocken eines Nicht-Admin-Nutzers
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Mocken eines vorhandenen Fachgruppe
        Fachgruppe existingFachgruppe = new Fachgruppe();
        existingFachgruppe.setId(1L);
        existingFachgruppe.setName("Alte Fachgruppe");
        existingFachgruppe.setReferentId(1L);
        existingFachgruppe.setStellvertreterId(2L);

        // Erwartung einer ResponseStatusException
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.updateFachgruppe("1", existingFachgruppe);
        });

        // Überprüfung, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Testet die Validierung einer Fachgruppe mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     * 
     * @return void
     */
    @Test
    public void testValidateFachgruppe_ValidInput_NoErrors() {
        // Arrange
        Fachgruppe validFachgruppe = new Fachgruppe();
        validFachgruppe.setName("Testfachbereich");
        validFachgruppe.setKuerzel("KF");

        Mitarbeiter referent = new Mitarbeiter();
        referent.setId(1L);
        validFachgruppe.setReferent(referent);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);
        validFachgruppe.setStellvertreter(stellvertreter);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        validFachgruppe.setFachbereich(fachbereich);

        // Prüfung
        List<String> errors = controller.validateFachgruppe(validFachgruppe);

        // Assertions
        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    /**
     * Testet die Validierung eines Fachbereichs mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param name             der Name des Fachbereichs
     * @param kuerzel          das Kuerzel des Fachbereichs
     * @param referentId       die Id des Referenten
     * @param stellvertreterId die Id des Stellvertreters
     * @param fachbereichId    die Id des Fachbereichs
     */
    @ParameterizedTest
    @CsvSource({
            ", KF123, 1, 2, 3", // Name fehlt
            "Testfachgruppe, , 1, 2, 3", // Kürzel fehlt
            "Testfachgruppe, KF123, , 2, 3", // Referent fehlt
            "Testfachgruppe, KF123, 1, , 3", // Stellvertreter fehlt
            "Testfachgruppe, KF123, 1, 2, " // Fachbereich fehlt
    })
    public void testValidateFachgruppe_InvalidInputs(String name, String kuerzel, String referentId,
            String stellvertreterId, String fachbereichId) {
        // Vorbereitung einer Fachgruppe mit den gegebenen Eingaben
        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setName(name);
        fachgruppe.setKuerzel(kuerzel);
        fachgruppe.setReferentId(referentId == null ? null : Long.parseLong(referentId));
        fachgruppe.setStellvertreterId(stellvertreterId == null ? null : Long.parseLong(stellvertreterId));
        fachgruppe.setFachbereichId(fachbereichId == null ? null : Long.parseLong(fachbereichId));

        // Ausführen der Validierung
        List<String> errors = controller.validateFachgruppe(fachgruppe);

        // Überprüfen, ob Fehler in der Fehlerliste enthalten sind
        assertTrue(!errors.isEmpty(), "Es sollte mindestens ein Validierungsfehler auftreten.");
    }
}
