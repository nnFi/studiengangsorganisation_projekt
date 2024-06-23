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
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.entity.Abschluss;
import com.projekt.studiengangsorganisation.service.StudiengangService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den StudiengangsController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 */
public class StudiengangControllerTest {

    @Mock
    private StudiengangService studiengangService;

    @Mock
    private MitarbeiterService mitarbeiterService;

    @Mock
    private FachbereichService fachbereichService;

    @Mock
    private NutzerService nutzerService;

    @InjectMocks
    private StudiengangController controller;

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
    public void testGetOne_ValidId_ReturnsStudiengang() {
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);

        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);

        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(studiengang));

        Studiengang result = controller.getOne("1");

        assertEquals(studiengang.getId(), result.getId());
        assertEquals(studiengang.getName(), result.getName());
        assertEquals(studiengang.getAbschluss(), result.getAbschluss());
        assertEquals(studiengang.getRegelstudienzeit(), result.getRegelstudienzeit());
    }

    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    @Test
    public void testGetAll_ReturnsAllStudiengaenge() {
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);

        Studiengang studiengang1 = new Studiengang();
        studiengang1.setId(1L);
        studiengang1.setName("Informatik");
        studiengang1.setLeiter(leiter);
        studiengang1.setStellvertretenderLeiter(stellvertreter);
        studiengang1.setFachbereich(fachbereich);

        Studiengang studiengang2 = new Studiengang();
        studiengang2.setId(2L);
        studiengang2.setName("Wirtschaftsinformatik");
        studiengang2.setLeiter(leiter);
        studiengang2.setStellvertretenderLeiter(stellvertreter);
        studiengang2.setFachbereich(fachbereich);

        List<Studiengang> studiengaenge = Arrays.asList(studiengang1, studiengang2);

        when(studiengangService.getStudiengaenge()).thenReturn(studiengaenge);

        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Studiengang> result = controller.getAll(response);

        assertEquals(2, result.size());
    }

    @Test
    public void testCreateStudiengang_Administrator_SuccessfullyCreated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);

        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiterId(1L);
        studiengang.setStellvertreterId(2L);
        studiengang.setFachbereichId(1L);

        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(leiter));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));
        when(studiengangService.saveAndFlush(any(Studiengang.class))).thenReturn(studiengang);

        ResponseEntity<Studiengang> response = controller.createStudiengang(studiengang);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(studiengang, response.getBody());
    }

    @Test
    public void testUpdateStudiengang_Administrator_SuccessfullyUpdated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);

        Studiengang existingStudiengang = new Studiengang();
        existingStudiengang.setId(1L);
        existingStudiengang.setName("Informatik");
        existingStudiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        existingStudiengang.setRegelstudienzeit(6);
        existingStudiengang.setLeiter(leiter);
        existingStudiengang.setStellvertretenderLeiter(stellvertreter);
        existingStudiengang.setFachbereich(fachbereich);

        Studiengang updatedStudiengang = new Studiengang();
        updatedStudiengang.setId(1L);
        updatedStudiengang.setName("Informatik");
        updatedStudiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        updatedStudiengang.setRegelstudienzeit(6);
        updatedStudiengang.setLeiterId(2L);
        updatedStudiengang.setStellvertreterId(1L);
        updatedStudiengang.setFachbereichId(1L);

        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(existingStudiengang));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(leiter));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));
        when(studiengangService.saveAndFlush(any(Studiengang.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Studiengang> response = controller.updateStudiengang("1", updatedStudiengang);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStudiengang.getName(), response.getBody().getName());
        assertEquals(updatedStudiengang.getAbschluss(), response.getBody().getAbschluss());
        assertEquals(updatedStudiengang.getRegelstudienzeit(), response.getBody().getRegelstudienzeit());
    }

    @Test
    public void testUpdateStudiengang_NotAuthorized_ThrowsResponseStatusException() {
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        Studiengang existingStudiengang = new Studiengang();
        existingStudiengang.setId(1L);
        existingStudiengang.setName("Informatik");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateStudiengang("1", existingStudiengang);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    public void testValidateStudiengang_ValidInputs() {
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);

        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);

        List<String> errors = controller.validateStudiengang(studiengang);

        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    @ParameterizedTest
    @CsvSource({
            ",BACHELOR_OF_SCIENCE,6,1,2,1",
            "Informatik,,6,1,2,1",
            "Informatik,BACHELOR_OF_SCIENCE,0,1,2,1",
            "Informatik,BACHELOR_OF_SCIENCE,6,,2,1",
            "Informatik,BACHELOR_OF_SCIENCE,6,1,,1",
            "Informatik,BACHELOR_OF_SCIENCE,6,1,2,"
    })
    public void testValidateStudiengang_InvalidInputs(String name, Abschluss abschluss, Integer regelstudienzeit,
            Long leiterId, Long stellvertreterId, Long fachbereichId) {
        Mitarbeiter leiter = null;
        Mitarbeiter stellvertreter = null;
        Fachbereich fachbereich = null;

        if (leiterId != null) {
            leiter = new Mitarbeiter();
            leiter.setId(leiterId);
            when(mitarbeiterService.getMitarbeiter(leiterId)).thenReturn(Optional.of(leiter));
        }

        if (stellvertreterId != null) {
            stellvertreter = new Mitarbeiter();
            stellvertreter.setId(stellvertreterId);
            when(mitarbeiterService.getMitarbeiter(stellvertreterId)).thenReturn(Optional.of(stellvertreter));
        }

        if (fachbereichId != null) {
            fachbereich = new Fachbereich();
            fachbereich.setId(fachbereichId);
            when(fachbereichService.getFachbereich(fachbereichId)).thenReturn(Optional.of(fachbereich));
        }

        Studiengang studiengang = new Studiengang();
        studiengang.setName(name);
        studiengang.setAbschluss(abschluss);
        studiengang.setRegelstudienzeit(regelstudienzeit != null ? regelstudienzeit : 0);
        if (leiter != null)
            studiengang.setLeiter(leiter);
        if (stellvertreter != null)
            studiengang.setStellvertretenderLeiter(stellvertreter);
        if (fachbereich != null)
            studiengang.setFachbereich(fachbereich);
        List<String> errors = controller.validateStudiengang(studiengang);

        assertFalse(errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}