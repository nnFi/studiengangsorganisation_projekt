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

public class MitarbeiterControllerTest {

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

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetOne_ValidId_ReturnsMitarbeiter() {
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setId(1L);
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(mockMitarbeiter));

        Mitarbeiter result = controller.getOne("1");

        assertEquals(mockMitarbeiter, result);
    }

    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    @Test
    public void testCreateMitarbeiter_Administrator_SuccessfullyCreated() {        
        // Mocking the authentication
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Setup security context to return admin user
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocking the Mitarbeiter
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setVorname("John");
        mockMitarbeiter.setNachname("Doe");
        mockMitarbeiter.setPassword("password");

        // Mocking the password encoding
        when(passwordEncoder.encode(mockMitarbeiter.getPassword())).thenReturn("encodedPassword");

        // Mocking the saveAndFlush method
        when(mitarbeiterService.saveAndFlush(any(Mitarbeiter.class))).thenReturn(mockMitarbeiter);

        // Perform the createMitarbeiter call
        ResponseEntity<Mitarbeiter> response = controller.createMitarbeiter(mockMitarbeiter);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateMitarbeiter_NonAdministrator_NotCreated() {
        // Mocking the authentication
        Nutzer user = new Mitarbeiter();
        user.setUsername("mia.mitarbeiter1");
        user.setRole("MITARBEITER");

        // Setup security context to return non-admin user
        when(authentication.getName()).thenReturn("mia.mitarbeiter1");
        when(nutzerService.getNutzerByUsername("mia.mitarbeiter1")).thenReturn(Optional.of(user));

        // Mocking the Mitarbeiter
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setVorname("John");
        mockMitarbeiter.setNachname("Doe");
        mockMitarbeiter.setPassword("password");

        // Perform the createMitarbeiter call and expect an exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createMitarbeiter(mockMitarbeiter);
        });

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @SuppressWarnings("null")
    @Test
    public void testUpdateMitarbeiter_Administrator_SuccessfullyUpdated() {
        // Mocking the authentication
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Setup security context to return admin user
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocking the existing Mitarbeiter
        Mitarbeiter existingMitarbeiter = new Mitarbeiter();
        existingMitarbeiter.setId(1L);
        existingMitarbeiter.setVorname("John");
        existingMitarbeiter.setNachname("Doe");

        // Mocking the updated Mitarbeiter
        Mitarbeiter updatedMitarbeiter = new Mitarbeiter();
        updatedMitarbeiter.setVorname("Jane");
        updatedMitarbeiter.setNachname("Doe");
        updatedMitarbeiter.setPassword("newpassword");

        // Mocking the getMitarbeiter method
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(existingMitarbeiter));

        // Mocking the saveAndFlush method
        when(mitarbeiterService.saveAndFlush(any(Mitarbeiter.class))).thenReturn(existingMitarbeiter);

        // Perform the updateMitarbeiter call
        ResponseEntity<Mitarbeiter> response = controller.updateMitarbeiter("1", updatedMitarbeiter);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jane", response.getBody().getVorname());
        assertEquals("Doe", response.getBody().getNachname());
    }

    @Test
    public void testUpdateMitarbeiter_NonAdministrator_NotUpdated() {
        // Mocking the authentication
        Nutzer user = new Mitarbeiter();
        user.setUsername("mia.mitarbeiter1");
        user.setRole("MITARBEITER");

        // Setup security context to return non-admin user
        when(authentication.getName()).thenReturn("mia.mitarbeiter1");
        when(nutzerService.getNutzerByUsername("mia.mitarbeiter1")).thenReturn(Optional.of(user));

        // Mocking the existing Mitarbeiter
        Mitarbeiter existingMitarbeiter = new Mitarbeiter();
        existingMitarbeiter.setId(1L);
        existingMitarbeiter.setVorname("John");
        existingMitarbeiter.setNachname("Doe");

        // Mocking the updated Mitarbeiter
        Mitarbeiter updatedMitarbeiter = new Mitarbeiter();
        updatedMitarbeiter.setVorname("Jane");
        updatedMitarbeiter.setNachname("Doe");
        updatedMitarbeiter.setPassword("newpassword");

        // Mocking the getMitarbeiter method
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(existingMitarbeiter));

        // Perform the updateMitarbeiter call and expect an exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateMitarbeiter("1", updatedMitarbeiter);
        });

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "John, Doe, Valid1!password",
            "Alice, Smith, AnotherValid1Passwo-rd",
            "Emily, Johnson, Secure!Pass123",
    })
    public void testValidateMitarbeiter_ValidInputs(String vorname, String nachname, String password) {
        // Stelle sicher, dass keine der Eingaben null ist
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

    @ParameterizedTest
    @CsvSource({
            ", Doe, Valid1!password", // Leerzeichen als Vorname
            "John, , Valid1!password", // Leerzeichen als Nachname
            ", , Valid1!password", // Leerzeichen als Vor- und Nachname
            "John, Doe, ", // Leerzeichen als Passwort
            "John, Doe, 123", // Passwort zu kurz
            "John, Doe, invalid", // Passwort enthält kein Sonderzeichen
            "John, Doe, invalidpassword", // Passwort enthält kein Großbuchstaben
            "John, Doe, invalidPassword", // Passwort enthält keine Zahl
            "John, Doe, invalidPassword!", // Passwort enthält keine Kleinbuchstaben
    })
    public void testValidateMitarbeiter_InvalidInputs(String vorname, String nachname, String password) {

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