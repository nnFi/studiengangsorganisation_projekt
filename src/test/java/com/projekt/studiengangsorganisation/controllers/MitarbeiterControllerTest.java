package com.projekt.studiengangsorganisation.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
        Nutzer mockNutzer = new Mitarbeiter();
        mockNutzer.setRole("ADMIN");
        when(nutzerService.getNutzerByUsername(any())).thenReturn(Optional.of(mockNutzer));
        
        Mitarbeiter mockMitarbeiter = new Mitarbeiter();
        mockMitarbeiter.setVorname("John");
        mockMitarbeiter.setNachname("Doe");
        mockMitarbeiter.setPassword("password");

        ResponseEntity<Mitarbeiter> response = controller.createMitarbeiter(mockMitarbeiter);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
