package com.projekt.studiengangsorganisation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FachgruppeControllerTest {

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

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test.user");
    }

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

    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken eines leeren Optional für ungültige ID
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

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
     * Testet die Methode updateFachbreich durch einen nicht-Admin-Nutzer.
     * Erwartet, dass der Fachbereich erfolgreich aktualisiert wird.
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
}
