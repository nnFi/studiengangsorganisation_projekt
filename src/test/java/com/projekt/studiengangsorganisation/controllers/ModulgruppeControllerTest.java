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
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den ModulgruppeController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 */
public class ModulgruppeControllerTest {

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
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetOne_ValidId_ReturnsModulgruppe() {
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setId(1L);
        modulgruppe.setName("Test Modulgruppe");

        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.of(modulgruppe));

        Modulgruppe result = controller.getOne("1");

        assertEquals(modulgruppe.getId(), result.getId());
        assertEquals(modulgruppe.getName(), result.getName());
    }

    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    @Test
    public void testGetAll_ReturnsAllModulgruppen() {
        Modulgruppe modulgruppe1 = new Modulgruppe();
        modulgruppe1.setId(1L);
        modulgruppe1.setName("Modulgruppe 1");

        Modulgruppe modulgruppe2 = new Modulgruppe();
        modulgruppe2.setId(2L);
        modulgruppe2.setName("Modulgruppe 2");

        List<Modulgruppe> modulgruppen = Arrays.asList(modulgruppe1, modulgruppe2);

        when(modulgruppeService.getModulgruppen()).thenReturn(modulgruppen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Modulgruppe> result = controller.getAll(response);

        assertEquals(2, result.size());
    }

    @Test
    public void testCreateModulgruppe_Administrator_SuccessfullyCreated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Test Modulgruppe");

        when(modulgruppeService.saveAndFlush(any(Modulgruppe.class))).thenReturn(modulgruppe);

        ResponseEntity<Modulgruppe> response = controller.createModulgruppe(modulgruppe);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(modulgruppe, response.getBody());
    }

    @Test
    public void testCreateModulgruppe_NotAuthorized_ThrowsResponseStatusException() {
        Nutzer user = new Admin();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Test Modulgruppe");

        when(modulgruppeService.saveAndFlush(any(Modulgruppe.class))).thenReturn(modulgruppe);

        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.createModulgruppe(modulgruppe);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testValidateModulgruppe_ValidInputs() {
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Testmodulgruppe");

        List<String> errors = controller.validateModulgruppe(modulgruppe);

        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "T"
    })
    public void testValidateModulgruppe_InvalidInputs(String name) {
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName(name);

        List<String> errors = controller.validateModulgruppe(modulgruppe);

        assertFalse(errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}