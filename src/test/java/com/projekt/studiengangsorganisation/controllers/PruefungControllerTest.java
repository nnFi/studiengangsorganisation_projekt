package com.projekt.studiengangsorganisation.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den PruefungController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden zu testen.
 * 
 */
public class PruefungControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
    @Mock
    private PruefungService pruefungService;

    @Mock
    private NutzerService nutzerService;

    @Mock
    private PruefungsordnungService pruefungsordnungService;

    @Mock
    private ModulService modulService;

    @InjectMocks
    private PruefungController controller;

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
        // Initialisierung der Mock-Objekte
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    /**
     * Testet die Methode getOne mit einer gültigen ID.
     * Erwartet, dass die Pruefung zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsPruefung() {
        // Mocken einer gültigen Prüfung mit zugehöriger Prüfungsordnung und Modul
        Pruefung mockPruefung = new Pruefung();
        mockPruefung.setId(1L);
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        mockPruefung.setPruefungsordnung(pruefungsordnung);
        Modul modul = new Modul();
        modul.setId(1L);
        mockPruefung.setModul(modul);

        // Mocken des Service-Aufrufs für getPruefung
        when(pruefungService.getPruefung("1")).thenReturn(Optional.of(mockPruefung));

        // Aufruf der Controller-Methode
        Pruefung result = controller.getOne("1");

        // Assertions
        assertEquals(mockPruefung, result);
        assertEquals(1L, result.getPruefungsordnungId());
        assertEquals(1L, result.getModulId());
    }

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken des Service-Aufrufs für getPruefung mit ungültiger ID (kein Ergebnis)
        when(pruefungService.getPruefung("1")).thenReturn(Optional.empty());

        // Prüfen, ob eine ResponseStatusException geworfen wird
        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode getAll.
     * Erwartet, dass alle Prüfungen zurückgegeben werden.
     * 
     * @return void
     */
    @Test
    public void testGetAll_ReturnsAllPruefungen() throws JsonProcessingException {
        // Mocken von zwei Prüfungen mit zugehörigen Prüfungsordnungen und Modulen
        Pruefung pruefung1 = new Pruefung();
        Pruefungsordnung pruefungsordnung1 = new Pruefungsordnung();
        pruefungsordnung1.setId(1L);
        pruefung1.setPruefungsordnung(pruefungsordnung1);
        Modul modul1 = new Modul();
        modul1.setId(1L);
        pruefung1.setModul(modul1);

        Pruefung pruefung2 = new Pruefung();
        Pruefungsordnung pruefungsordnung2 = new Pruefungsordnung();
        pruefungsordnung2.setId(2L);
        pruefung2.setPruefungsordnung(pruefungsordnung2);
        Modul modul2 = new Modul();
        modul2.setId(2L);
        pruefung2.setModul(modul2);
        
        // Erstelle eine Liste von Pruefung-Objekten, die pruefung1 und pruefung2 enthält
        List<Pruefung> pruefungen = Arrays.asList(pruefung1, pruefung2);

        // Mocken des Service-Aufrufs für getPruefungen
        when(pruefungService.getPruefungen()).thenReturn(pruefungen);

        // Mocken des HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Die Methode getAll() des Controllers aufrufen, um alle Pruefungen abzurufen,
        // ohne einen spezifischen Filter anzuwenden.
        List<Pruefung> result = controller.getAll(response, null);

        // Assertions
        assertEquals(2, result.size());
    }

    /**
     * Testet die Methode createPruefung durch einen Administrator.
     * Erwartet, dass die Pruefung erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreatePruefung_Administrator_SuccessfullyCreated() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Mocken der Authentifizierung
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocken der Prüfung
        Pruefung pruefung = new Pruefung();
        pruefung.setId(1L);
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        List<Pruefung> pruefungsListe = new ArrayList<>();
        pruefungsordnung.setPruefungen(pruefungsListe);
        pruefung.setPruefungsordnung(pruefungsordnung);

        // Mocken des Moduls
        Modul modul = new Modul();
        modul.setId(1L);
        pruefung.setModul(modul);

        // Mocken des Service-Aufrufs für getPruefungsordnung und getModul
        when(pruefungsordnungService.getPruefungsordnung(pruefung.getPruefungsordnungId())).thenReturn(Optional.of(pruefungsordnung));
        when(modulService.getModul(pruefung.getModulId())).thenReturn(Optional.of(modul));

        // Mocken des Service-Aufrufs für saveAndFlush
        when(pruefungService.saveAndFlush(any(Pruefung.class))).thenReturn(pruefung);

        // Aufruf der Controller-Methode
        ResponseEntity<Pruefung> response = controller.createPruefung(pruefung);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Testet die Methode createPruefung durch eine nicht berechtigte Person.
     * Erwartet, dass die Pruefung nicht erstellt wird und eine Ausnahme ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testCreatePruefung_NotAuthorized_ThrowsResponseStatusException() {
        // Mocken eines nicht autorisierten Nutzers (Mitarbeiter)
        Nutzer nutzer = new Student();
        nutzer.setUsername("sam.student");
        nutzer.setRole("STUDENT");

        // Mocken der Authentifizierung
        when(authentication.getName()).thenReturn("sam.student");
        when(nutzerService.getNutzerByUsername("sam.student")).thenReturn(Optional.of(nutzer));

        // Erstellen einer neuen Prüfung
        Pruefung pruefung = new Pruefung();
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        pruefung.setPruefungsordnung(pruefungsordnung);
        Modul modul = new Modul();
        modul.setId(1L);
        pruefung.setModul(modul);

        // Prüfen, ob eine ResponseStatusException geworfen wird
        assertThrows(ResponseStatusException.class, () -> {
            controller.createPruefung(pruefung);
        });

        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.createPruefung(pruefung);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Testet die Methode createPruefung durch eine bereits vorhandene Prüfung.
     * Erwartet, dass die Pruefung nicht erstellt wird und eine Ausnahme ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testCreatePruefung_ExistingPruefung_ThrowsResponseStatusException() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Mocken der Authentifizierung
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Erstellen einer neuen Prüfung
        Pruefung pruefung = new Pruefung();
        pruefung.setModulId(1L);
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        pruefung.setPruefungsordnung(pruefungsordnung);
        Modul modul = new Modul();
        modul.setId(1L);
        pruefung.setModul(modul);

        // Mocken des Service-Aufrufs für getPruefungsordnung und getModul
        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.of(pruefungsordnung));
        when(modulService.getModul(1L)).thenReturn(Optional.of(modul));

        // Mocken einer bereits existierenden Prüfung in der Prüfungsordnung
        Pruefung existingPruefung = new Pruefung();
        existingPruefung.setModul(modul);

        pruefungsordnung.setPruefungen(Arrays.asList(existingPruefung));

        // Prüfen, ob eine ResponseStatusException geworfen wird
        assertThrows(ResponseStatusException.class, () -> {
            controller.createPruefung(pruefung);
        });
    }

    /**
     * Testet die Methode createPruefung mit einer freigegebenen Prüfungsordnung.
     * Erwartet, dass die Pruefung nicht erstellt wird und eine Ausnahme ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testCreatePruefung_PruefungsordnungReleased_ThrowsResponseStatusException() {
        // Mocken eines Admin-Nutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Mocken der Authentifizierung
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Erstellen einer neuen Prüfung mit freigegebener Prüfungsordnung
        Pruefung pruefung = new Pruefung();
        pruefung.setModulId(1L);
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setId(1L);
        pruefungsordnung.setFreigegeben(true); // Setzen der Freigabe auf true
        pruefung.setPruefungsordnung(pruefungsordnung);
        Modul modul = new Modul();
        modul.setId(1L);
        pruefung.setModul(modul);

        // Mocken des Service-Aufrufs für getPruefungsordnung und getModul
        when(pruefungsordnungService.getPruefungsordnung(1L)).thenReturn(Optional.of(pruefungsordnung));
        when(modulService.getModul(1L)).thenReturn(Optional.of(modul));

        // Prüfen, ob eine ResponseStatusException geworfen wird
        assertThrows(ResponseStatusException.class, () -> {
            controller.createPruefung(pruefung);
        });
    }
}