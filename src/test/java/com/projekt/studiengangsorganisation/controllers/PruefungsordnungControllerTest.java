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
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;
import com.projekt.studiengangsorganisation.service.StudiengangService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den PruefungsordnungController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
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

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetOne_ValidId_ReturnsPruefungsordnung() {
        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);

        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setFreigegeben(true);
        pruefungsordnung.setStudiengang(studiengang);

        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.of(pruefungsordnung));

        Pruefungsordnung result = controller.getOne("1");

        assertEquals(pruefungsordnung.getId(), result.getId());
        assertEquals(pruefungsordnung.getVersion(), result.getVersion());
        assertEquals(pruefungsordnung.isFreigegeben(), result.isFreigegeben());
    }

    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    @Test
    public void testGetAll_ReturnsAllPruefungsordnungen() {
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

        when(pruefungsordnungService.getPruefungsordnungen()).thenReturn(pruefungsordnungen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Pruefungsordnung> result = controller.getAll(response);

        assertEquals(2, result.size());
    }

    @Test
    public void testCreatePruefungsordnung_Administrator_SuccessfullyCreated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setFreigegeben(true);
        pruefungsordnung.setStudiengangId(1L);

        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);

        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(studiengang));
        when(pruefungsordnungService.saveAndFlush(any(Pruefungsordnung.class))).thenReturn(pruefungsordnung);

        ResponseEntity<Pruefungsordnung> response = controller.createPruefungsordnung(pruefungsordnung);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pruefungsordnung, response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    public void testUpdatePruefungsordnung_Administrator_SuccessfullyUpdated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Pruefungsordnung existingPruefungsordnung = new Pruefungsordnung();
        existingPruefungsordnung.setId(1L);
        existingPruefungsordnung.setFreigegeben(false);
        existingPruefungsordnung.setAuslaufend(false);

        Pruefungsordnung updatedPruefungsordnung = new Pruefungsordnung();
        updatedPruefungsordnung.setId(1L);
        updatedPruefungsordnung.setFreigegeben(true);
        updatedPruefungsordnung.setAuslaufend(true);

        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.of(existingPruefungsordnung));
        when(pruefungsordnungService.saveAndFlush(any(Pruefungsordnung.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Pruefungsordnung> response = controller.updatePruefungsordnung("1", updatedPruefungsordnung);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPruefungsordnung.isFreigegeben(), response.getBody().isFreigegeben());
        assertEquals(updatedPruefungsordnung.isAuslaufend(), response.getBody().isAuslaufend());
    }

    @Test
    public void testUpdatePruefungsordnung_NotAuthorized_ThrowsResponseStatusException() {
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        Pruefungsordnung existingPruefungsordnung = new Pruefungsordnung();
        existingPruefungsordnung.setId(1L);
        existingPruefungsordnung.setVersion("1.0");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updatePruefungsordnung("1", existingPruefungsordnung);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void testValidatePruefungsordnung_ValidInputs() {
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion("1.0");
        pruefungsordnung.setStudiengangId(1L);

        List<String> errors = controller.validatePruefungsordnung(pruefungsordnung);

        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    @ParameterizedTest
    @CsvSource({
            ",1",
            "1.0,"
    })
    public void testValidatePruefungsordnung_InvalidInputs(String version, String studiengangId) {
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setVersion(version);
        pruefungsordnung.setStudiengangId(studiengangId == null ? null : Long.parseLong(studiengangId));

        List<String> errors = controller.validatePruefungsordnung(pruefungsordnung);

        assertFalse(errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}