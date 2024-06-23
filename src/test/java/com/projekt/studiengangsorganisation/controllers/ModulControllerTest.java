package com.projekt.studiengangsorganisation.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.projekt.studiengangsorganisation.entity.Abschluss;
import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.ModulArt;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Sprache;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Testklasse für den ModulController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 */
public class ModulControllerTest {

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
    @Mock
    private ModulService modulService;

    @Mock
    private NutzerService nutzerService;

    @Mock
    private FachgruppeService fachgruppeService;

    @Mock
    private MitarbeiterService mitarbeiterService;

    @Mock
    private ModulgruppeService modulgruppeService;

    @InjectMocks
    private ModulController controller;

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

    @Test
    public void testGetOne_ValidId_ReturnsModul() {
        Modul modul = new Modul();
        modul.setId(1L);
        modul.setName("Test Modul");
        modul.setModulbeauftragter(new Mitarbeiter());
        modul.setFachgruppe(new Fachgruppe());
        modul.setModulgruppe(new Modulgruppe());

        when(modulService.getModul(1L)).thenReturn(Optional.of(modul));

        Modul result = controller.getOne("1");

        assertEquals(modul.getId(), result.getId());
        assertEquals(modul.getName(), result.getName());
    }

    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        when(modulService.getModul(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    @Test
    public void testGetAll_ReturnsAllModule() throws JsonMappingException, JsonProcessingException {
        Modul modul1 = new Modul();
        modul1.setId(1L);
        modul1.setName("Modul 1");
        modul1.setModulbeauftragter(new Mitarbeiter());
        modul1.setFachgruppe(new Fachgruppe());
        modul1.setModulgruppe(new Modulgruppe());

        Modul modul2 = new Modul();
        modul2.setId(2L);
        modul2.setName("Modul 2");
        modul2.setModulbeauftragter(new Mitarbeiter());
        modul2.setFachgruppe(new Fachgruppe());
        modul2.setModulgruppe(new Modulgruppe());

        List<Modul> module = Arrays.asList(modul1, modul2);

        when(modulService.getModule()).thenReturn(module);

        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Modul> result = controller.getAll(response, null);

        assertEquals(2, result.size());
    }

    @Test
    public void testCreateModul_AuthorizedUser_SuccessfullyCreated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Modul modul = new Modul();
        modul.setName("Test Modul");
        modul.setWorkload(100);
        modul.setCredits(5);
        modul.setDauer(1);
        modul.setArt(ModulArt.VORLESUNG);
        modul.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        modul.setBeschreibung("Test Beschreibung");
        modul.setLehrveranstaltungsort("Test Ort");
        modul.setSprache(Sprache.DEUTSCH);
        modul.setFachgruppeId(1L);
        modul.setModulbeauftragterId(1L);
        modul.setModulgruppeId(1L);

        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setId(1L);
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setReferentId(admin.getId());
        fachgruppe.setFachbereich(fachbereich);

        Mitarbeiter modulbeauftragter = new Mitarbeiter();
        modulbeauftragter.setId(1L);

        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setId(1L);

        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.of(fachgruppe));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(modulbeauftragter));
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.of(modulgruppe));
        when(modulService.saveAndFlush(any(Modul.class))).thenReturn(modul);

        ResponseEntity<Modul> response = controller.createModul(modul);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(modul, response.getBody());
    }

    @Test
    public void testUpdateModul_AuthorizedUser_SuccessfullyUpdated() {
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");
        admin.setId(1L);

        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        Mitarbeiter mitarbeiter1 = new Mitarbeiter();
        mitarbeiter1.setId(1L);

        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setId(1L);

        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setId(1L);
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setReferentId(admin.getId());
        fachbereich.setStellvertreterId(2L);
        fachgruppe.setFachbereich(fachbereich);
        fachgruppe.setReferent(mitarbeiter1);
        fachgruppe.setStellvertreter(new Mitarbeiter());

        Modul existingModul = new Modul();
        existingModul.setId(1L);
        existingModul.setName("Test Modul");
        existingModul.setWorkload(100);
        existingModul.setCredits(5);
        existingModul.setDauer(1);
        existingModul.setArt(ModulArt.VORLESUNG);
        existingModul.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        existingModul.setBeschreibung("Test Beschreibung");
        existingModul.setLehrveranstaltungsort("Test Ort");
        existingModul.setSprache(Sprache.DEUTSCH);
        existingModul.setFachgruppe(fachgruppe);
        existingModul.setModulbeauftragter(mitarbeiter1);
        existingModul.setModulgruppe(modulgruppe);
        existingModul.setFreigegeben(false);

        Modul updatedModul = new Modul();
        updatedModul.setId(1L);
        updatedModul.setName("Test Modul");
        updatedModul.setWorkload(150);
        updatedModul.setCredits(6);
        updatedModul.setDauer(2);
        updatedModul.setArt(ModulArt.VORLESUNG);
        updatedModul.setAbschluss(Abschluss.MASTER_OF_SCIENCE);
        updatedModul.setBeschreibung("Updated Beschreibung");
        updatedModul.setLehrveranstaltungsort("Updated Ort");
        updatedModul.setSprache(Sprache.ENGLISCH);
        updatedModul.setFachgruppeId(1L);
        updatedModul.setModulbeauftragterId(1L);
        updatedModul.setModulgruppeId(1L);
        updatedModul.setFreigegeben(true);

        when(modulService.getModul(1L)).thenReturn(Optional.of(existingModul));
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.of(fachgruppe));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(mitarbeiter1));
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.of(modulgruppe));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(mitarbeiter1));

        ResponseEntity<Modul> response = controller.updateModul("1", updatedModul);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Modul resultModul = response.getBody();
        assertNotNull(resultModul);
        assertEquals(150, resultModul.getWorkload());
        assertEquals(6, resultModul.getCredits());
        assertEquals(2, resultModul.getDauer());
        assertEquals("Updated Beschreibung", resultModul.getBeschreibung());
        assertEquals("Updated Ort", resultModul.getLehrveranstaltungsort());
        assertNotNull(resultModul.getFachgruppe());
        assertNotNull(resultModul.getModulbeauftragter());
        assertNotNull(resultModul.getModulgruppe());
        assertEquals(existingModul.isFreigegeben(), resultModul.isFreigegeben());
    }

    @Test
    public void testUpdateModul_UnauthorizedUser_ThrowsResponseStatusException() {
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        Modul existingModul = new Modul();
        existingModul.setId(1L);
        existingModul.setName("Altes Modul");

        when(modulService.getModul(1L)).thenReturn(Optional.of(existingModul));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateModul("1", existingModul);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    public void testValidateModul_ValidInputs() {
        Modul modul = new Modul();
        modul.setName("Test Modul");
        modul.setWorkload(100);
        modul.setCredits(5);
        modul.setDauer(1);
        modul.setArt(ModulArt.VORLESUNG);
        modul.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        modul.setBeschreibung("Test Beschreibung");
        modul.setLehrveranstaltungsort("Test Ort");
        modul.setSprache(Sprache.DEUTSCH);
        modul.setFachgruppe(new Fachgruppe());
        modul.setModulbeauftragter(new Mitarbeiter());
        modul.setModulgruppe(new Modulgruppe());

        List<String> errors = controller.validateModul(modul);

        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    @ParameterizedTest
    @CsvSource({
            ", 100, 5, 1, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, Ort, DEUTSCH",
            "T, 100, 5, 1, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, Ort, DEUTSCH",
            "Test, 0, 5, 1, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, Ort, DEUTSCH",
            "Test, 100, 0, 1, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, Ort, DEUTSCH",
            "Test, 100, 5, 0, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, Ort, DEUTSCH",
            "Test, 100, 5, 1, , BACHELOR_OF_SCIENCE, Beschreibung, Ort, DEUTSCH",
            "Test, 100, 5, 1, VORLESUNG, , Beschreibung, Ort, DEUTSCH",
            "Test, 100, 5, 1, VORLESUNG, BACHELOR_OF_SCIENCE, , Ort, DEUTSCH",
            "Test, 100, 5, 1, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, , DEUTSCH",
            "Test, 100, 5, 1, VORLESUNG, BACHELOR_OF_SCIENCE, Beschreibung, Ort, "
    })
    public void testValidateModul_InvalidInputs(String name, Integer workload, Integer credits, Integer dauer,
            String art, String abschluss, String beschreibung,
            String lehrveranstaltungsort, String sprache) {
        Modul modul = new Modul();
        modul.setName(name);
        modul.setWorkload(workload != null ? workload : 0);
        modul.setCredits(credits != null ? credits : 0);
        modul.setDauer(dauer != null ? dauer : 0);
        modul.setArt(art != null ? ModulArt.valueOf(art) : null);
        modul.setAbschluss(abschluss != null ? Abschluss.valueOf(abschluss) : null);
        modul.setBeschreibung(beschreibung);
        modul.setLehrveranstaltungsort(lehrveranstaltungsort);
        modul.setSprache(sprache != null ? Sprache.valueOf(sprache) : null);

        List<String> errors = controller.validateModul(modul);

        assertFalse(errors.isEmpty(), "Es sollte mindestens ein Validierungsfehler auftreten.");
    }
}