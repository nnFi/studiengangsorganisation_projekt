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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;
import com.projekt.studiengangsorganisation.service.StudiengangService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den PruefungsordnungController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 * 
 * @author Finn Plassmeier
 */
public class PruefungsordnungControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
    @Mock
    private PruefungsordnungService pruefungsordnungService;

    @Mock
    private StudiengangService studiengangService;

    @Mock
    private NutzerService nutzerService;

    @InjectMocks
    private PruefungsordnungController controller;

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
     * Erwartet, dass die Pruefungsordnung zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsPruefungsordnung() {
        // Mocken eines Studiengangs
        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);

        // Mocken einer Pruefungsordnung mit gültiger ID
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setFreigegeben(true);
        pruefungsordnung.setStudiengang(studiengang);

        // Wenn der Service aufgerufen wird, geben Sie die modellierte Pruefungsordnung
        // zurück
        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.of(pruefungsordnung));

        // Aufruf der Controller-Methode, um die Pruefungsordnung mit ID "1" zu erhalten
        Pruefungsordnung result = controller.getOne("1");

        // Assertions, um sicherzustellen, dass die zurückgegebene Pruefungsordnung
        // korrekt ist
        assertEquals(pruefungsordnung.getId(), result.getId());
        assertEquals(pruefungsordnung.getVersion(), result.getVersion());
        assertTrue(pruefungsordnung.isFreigegeben() == result.isFreigegeben());
    }

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken eines leeren Optional für ungültige Pruefungsordnungs-ID
        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.empty());

        // Erwartung, dass eine ResponseStatusException geworfen wird
        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode getAll.
     * Erwartet, dass alle Pruefungsordnungen zurückgegeben werden.
     * 
     * @return void
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @Test
    public void testGetAll_ReturnsAllPruefungsordnungen() throws JsonMappingException, JsonProcessingException {
        // Mocken von zwei Studiengängen und zugehörigen Pruefungsordnungen
        Studiengang studiengang1 = new Studiengang();
        studiengang1.setId(1L);

        Pruefungsordnung pruefungsordnung1 = new Pruefungsordnung();
        pruefungsordnung1.setId(1L);
        pruefungsordnung1.setVersion("1.0");
        pruefungsordnung1.setStudiengang(studiengang1);

        Studiengang studiengang2 = new Studiengang();
        Pruefungsordnung pruefungsordnung2 = new Pruefungsordnung();
        pruefungsordnung2.setId(2L);
        pruefungsordnung2.setVersion("2.0");
        pruefungsordnung2.setStudiengang(studiengang2);

        List<Pruefungsordnung> pruefungsordnungen = Arrays.asList(pruefungsordnung1, pruefungsordnung2);

        // Wenn der Service aufgerufen wird, geben Sie die modellierten
        // Pruefungsordnungen zurück
        when(pruefungsordnungService.getPruefungsordnungen()).thenReturn(pruefungsordnungen);

        // Mocken des HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Aufruf der Controller-Methode, um alle Pruefungsordnungen zu erhalten
        List<Pruefungsordnung> result = controller.getAll(response, "{}");

        // Assertions, um sicherzustellen, dass die richtige Anzahl von
        // Pruefungsordnungen zurückgegeben wurde
        assertEquals(2, result.size());
    }

    /**
     * Testet die Methode createPruefungsordnung durch einen Administrator.
     * Erwartet, dass die Pruefungsordnung erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreatePruefungsordnung_Administrator_SuccessfullyCreated() {
        // Mocken eines Administrators
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Mocken der Authentifizierung und des zugehörigen Nutzerservices
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken einer neuen Pruefungsordnung
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setFreigegeben(true);
        pruefungsordnung.setStudiengangId(1L);

        // Mocken eines zugehörigen Studiengangs
        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);

        // Wenn der Studiengang-Service aufgerufen wird, geben Sie den mockierten
        // Studiengang zurück
        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(studiengang));
        // Wenn der Pruefungsordnung-Service aufgerufen wird, geben Sie die mockierte
        // Pruefungsordnung zurück
        when(pruefungsordnungService.saveAndFlush(any(Pruefungsordnung.class))).thenReturn(pruefungsordnung);

        // Aufruf der Controller-Methode, um eine Pruefungsordnung zu erstellen
        ResponseEntity<Pruefungsordnung> response = controller.createPruefungsordnung(pruefungsordnung);

        // Assertions, um sicherzustellen, dass die Pruefungsordnung erfolgreich
        // erstellt wurde
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pruefungsordnung, response.getBody());
    }

    /**
     * Testet die Methode createPruefungsordnung durch einen Studenten.
     * Erwartet, dass die Pruefungsordnung nicht erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreatePruefungsordnung_Student_NotAuthorized() {
        // Mocken eines nicht autorisierten Nutzers (Student)
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        // Mocken der Authentifizierung und des zugehörigen Nutzerservices
        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Mocken einer neuen Pruefungsordnung
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setFreigegeben(true);
        pruefungsordnung.setStudiengangId(1L);

        // Mocken eines zugehörigen Studiengangs
        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);

        // Wenn der Studiengang-Service aufgerufen wird, geben Sie den mockierten
        // Studiengang zurück
        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(studiengang));
        // Wenn der Pruefungsordnung-Service aufgerufen wird, geben Sie die mockierte
        // Pruefungsordnung zurück
        when(pruefungsordnungService.saveAndFlush(any(Pruefungsordnung.class))).thenReturn(pruefungsordnung);

        // Erwartung, dass eine ResponseStatusException geworfen wird, wenn ein Student
        // versucht, eine Pruefungsordnung zu erstellen
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createPruefungsordnung(pruefungsordnung);
        });

        // Überprüfen, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    /**
     * Testet die Methode updatePruefungsordnung durch einen Administrator.
     * Erwartet, dass die Pruefungsordnung erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @SuppressWarnings("null")
    @Test
    public void testUpdatePruefungsordnung_Administrator_SuccessfullyUpdated() {
        // Mocken eines Administrators
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Mocken der Authentifizierung und des zugehörigen Nutzerservices
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken einer bestehenden Pruefungsordnung und einer aktualisierten
        // Pruefungsordnung
        Pruefungsordnung existingPruefungsordnung = new Pruefungsordnung();
        existingPruefungsordnung.setId(1L);
        existingPruefungsordnung.setFreigegeben(false);
        existingPruefungsordnung.setAuslaufend(false);

        Pruefungsordnung updatedPruefungsordnung = new Pruefungsordnung();
        updatedPruefungsordnung.setId(1L);
        updatedPruefungsordnung.setFreigegeben(true);
        updatedPruefungsordnung.setAuslaufend(true);

        // Wenn der Pruefungsordnung-Service aufgerufen wird, geben Sie die mockierte
        // bestehende Pruefungsordnung zurück
        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.of(existingPruefungsordnung));
        // Wenn der Pruefungsordnung-Service aufgerufen wird, geben Sie die
        // aktualisierte Pruefungsordnung zurück
        when(pruefungsordnungService.saveAndFlush(any(Pruefungsordnung.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Aufruf der Controller-Methode, um eine Pruefungsordnung zu aktualisieren
        ResponseEntity<Pruefungsordnung> response = controller.updatePruefungsordnung("1", updatedPruefungsordnung);

        // Assertions, um sicherzustellen, dass die Pruefungsordnung erfolgreich
        // aktualisiert wurde
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPruefungsordnung.isFreigegeben(), response.getBody().isFreigegeben());
        assertEquals(updatedPruefungsordnung.isAuslaufend(), response.getBody().isAuslaufend());

    }

    /**
     * Testet die Methode updatePruefungsordnung durch einen nicht-Admin-Nutzer.
     * Erwartet, dass dere Pruefungsordnung nicht aktualisiert wird.
     * 
     * @return void
     */
    @Test
    public void testUpdatePruefungsordnung_NotAuthorized_ThrowsResponseStatusException() {
        // Mocken eines nicht autorisierten Nutzers (Student)
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        // Mocken der Authentifizierung und des zugehörigen Nutzerservices
        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Mocken einer bestehenden Pruefungsordnung
        Pruefungsordnung existingPruefungsordnung = new Pruefungsordnung();
        existingPruefungsordnung.setId(1L);
        existingPruefungsordnung.setVersion("1.0");

        // Erwartung, dass eine ResponseStatusException geworfen wird, wenn ein Student
        // versucht, eine Pruefungsordnung zu aktualisieren
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updatePruefungsordnung("1", existingPruefungsordnung);
        });

        // Überprüfen, ob der Statuscode der Exception korrekt ist
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    /**
     * Testet die Validierung einer Pruefungsordnung mit gültigen Eingaben.
     * 
     * @return void
     */
    @Test
    public void testValidatePruefungsordnung_ValidInputs() {
        // Mocken einer Pruefungsordnung mit gültigen Eingaben
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setStudiengangId(1L);

        // Aufruf der Controller-Methode, um die Validierung der Pruefungsordnung
        // durchzuführen
        List<String> errors = controller.validatePruefungsordnung(pruefungsordnung);

        // Assertion, dass keine Validierungsfehler auftreten sollten
        assertTrue(errors.isEmpty(), "Es sollten keine Validierungsfehler auftreten.");
    }

    /**
     * Testet die Validierung einer Pruefungsordnung mit ungültigen Eingaben.
     * 
     * @param version       Die Version der Pruefungsordnung
     * @param studiengangId Die ID des zugehörigen Studiengangs
     */
    @ParameterizedTest
    @CsvSource({
            ",1",
            "1.0,"
    })
    public void testValidatePruefungsordnung_InvalidInputs(String version, String studiengangId) {
        // Mocken einer Pruefungsordnung mit ungültigen Eingaben
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion(version);
        pruefungsordnung.setStudiengangId(studiengangId == null ? null : Long.parseLong(studiengangId));

        // Aufruf der Controller-Methode, um die Validierung der Pruefungsordnung
        // durchzuführen
        List<String> errors = controller.validatePruefungsordnung(pruefungsordnung);

        // Assertion, dass Validierungsfehler auftreten sollten
        assertFalse(errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}