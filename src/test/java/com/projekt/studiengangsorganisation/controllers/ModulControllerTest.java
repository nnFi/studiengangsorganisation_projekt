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
 * 
 * @author Paul Rakow
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

    /**
     * Testet die Methode getOne mit einer gültigen ID.
     * Erwartet, dass ein Modul zurückgegeben wird.
     * 
     * @return void
     */
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

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken des Verhaltens des ModulService für das Abrufen eines Moduls mit
        // ungültiger ID
        when(modulService.getModul(1L)).thenReturn(Optional.empty());

        // Auslösen der Methode getOne und Erwartung einer ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode getAll.
     * Erwartet, dass alle Module zurückgegeben werden.
     * 
     * @return void
     */
    @Test
    public void testGetAll_ReturnsAllModule() throws JsonMappingException, JsonProcessingException {
        // Erstellen von zwei Modul-Objekten für den Test
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

        // Erstellen einer Liste mit den beiden Modul-Objekten
        List<Modul> module = Arrays.asList(modul1, modul2);

        // Mocken des Verhaltens des ModulService für das Abrufen aller Module
        when(modulService.getModule()).thenReturn(module);

        // Mocken der HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Aufrufen der Controller-Methode getAll, um die Module zu erhalten
        List<Modul> result = controller.getAll(response, null);

        // Überprüfen, ob die zurückgegebene Liste die erwartete Größe von 2 hat
        assertEquals(2, result.size());
    }

    /**
     * Testet die Methode createModul durch einen Administrator.
     * Erwartet, dass ein Modul erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateModul_AuthorizedUser_SuccessfullyCreated() {
        // Erstellen eines Admin-Nutzerobjekts für den autorisierten Benutzer
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setUsername("test.mitabeiter");
        mitarbeiter.setRole("MITARBEITER");

        // Mocken des Verhaltens der Authentication und NutzerService für den
        // autorisierten Benutzer
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(mitarbeiter));

        // Erstellen eines Modul-Objekts mit gültigen Eingabewerten
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

        // Erstellen einer Fachgruppe und eines Modulbeauftragten für das Modul
        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setId(1L);
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setReferentId(mitarbeiter.getId());
        fachbereich.setReferent(mitarbeiter);
        fachgruppe.setFachbereich(fachbereich);

        Mitarbeiter modulbeauftragter = new Mitarbeiter();
        modulbeauftragter.setId(1L);

        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setId(1L);

        // Mocken des Verhaltens der Service-Klassen für die Fachgruppe, den
        // Modulbeauftragten und die Modulgruppe
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.of(fachgruppe));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(modulbeauftragter));
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.of(modulgruppe));

        // Mocken des Verhaltens des ModulService für das Speichern und Aktualisieren
        // des Moduls
        when(modulService.saveAndFlush(any(Modul.class))).thenReturn(modul);

        // Aufrufen der Controller-Methode createModul, um ein Modul zu erstellen
        ResponseEntity<Modul> response = controller.createModul(modul);

        // Überprüfen, ob der HTTP-Statuscode der erwarteten CREATED entspricht
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Überprüfen, ob das zurückgegebene Modul dem erwarteten Modul entspricht
        assertEquals(modul, response.getBody());
    }

    /**
     * Testet die Methode createModul durch einen Studenten.
     * Erwartet, dass kein Modul nicht erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateModul_Student_Unauthorized() {
        // Erstellen eines nicht autorisierten Nutzers (z.B. Student)
        Nutzer student = new Student();
        student.setUsername("test.student");
        student.setRole("STUDENT");

        // Mocken des Verhaltens der Authentication und NutzerService
        when(authentication.getName()).thenReturn("test.student");
        when(nutzerService.getNutzerByUsername("test.student")).thenReturn(Optional.of(student));

        // Erstellen eines Modul-Objekts
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

        // Mocken des Verhaltens der Service-Methoden für Fachgruppe, Mitarbeiter und
        // Modulgruppe
        // Hier werden keine Mock-Daten zurückgegeben, da der Nutzer nicht autorisiert
        // ist
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.empty());
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.empty());
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.empty());

        // Erwarten einer ResponseStatusException beim Versuch, das Modul zu erstellen
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createModul(modul);
        });

        // Überprüfen, ob der HTTP-Statuscode UNAUTHORIZED zurückgegeben wird
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    /**
     * Testet die Methode updateModul durch einen Administrator.
     * Erwartet, dass das Modul erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateModul_AuthorizedUser_SuccessfullyUpdated() {
        // Erstellen eines Admin-Nutzerobjekts für den autorisierten Benutzer
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");
        admin.setId(1L);

        // Mocken des Verhaltens der Authentication und NutzerService für den
        // autorisierten Benutzer
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Erstellen eines Mitarbeiters für den Modulbeauftragten
        Mitarbeiter mitarbeiter1 = new Mitarbeiter();
        mitarbeiter1.setId(1L);

        // Erstellen einer Modulgruppe
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setId(1L);

        // Erstellen einer Fachgruppe für das Modul
        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setId(1L);
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setReferentId(admin.getId());
        fachbereich.setStellvertreterId(2L);
        fachgruppe.setFachbereich(fachbereich);
        fachgruppe.setReferent(mitarbeiter1);
        fachgruppe.setStellvertreter(new Mitarbeiter());

        // Erstellen des vorhandenen Modul-Objekts
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

        // Erstellen des aktualisierten Modul-Objekts
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

        // Mocken des Verhaltens der Service-Klassen für das Abrufen und Speichern des
        // Moduls
        when(modulService.getModul(1L)).thenReturn(Optional.of(existingModul));
        when(fachgruppeService.getFachgruppe(1L)).thenReturn(Optional.of(fachgruppe));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(mitarbeiter1));
        when(modulgruppeService.getModulgruppe(1L)).thenReturn(Optional.of(modulgruppe));

        // Mocken des Verhaltens des ModulService für das Speichern und Aktualisieren
        // des Moduls
        when(modulService.saveAndFlush(any(Modul.class))).thenReturn(updatedModul);

        // Aufrufen der Controller-Methode updateModul, um das Modul zu aktualisieren
        ResponseEntity<Modul> response = controller.updateModul("1", updatedModul);

        // Überprüfen, ob der HTTP-Statuscode der erwarteten OK entspricht
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Überprüfen, ob das zurückgegebene Modul den erwarteten aktualisierten Werten
        // entspricht
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

    /**
     * Testet die Methode updateModul durch einen nicht-Admin-Nutzer.
     * Erwartet, dass das Modul nicht aktualisiert wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateModul_UnauthorizedUser_ThrowsResponseStatusException() {
        // Erstellen eines Nutzer-Objekts mit Studentenrolle
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");

        // Mocken des Verhaltens der Authentication und NutzerService
        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));

        // Erstellen eines vorhandenen Modul-Objekts
        Modul existingModul = new Modul();
        existingModul.setId(1L);
        existingModul.setName("Altes Modul");

        // Mocken des Verhaltens des ModulService für das Abrufen des Moduls
        when(modulService.getModul(1L)).thenReturn(Optional.of(existingModul));

        // Auslösen der Methode updateModul und Erwartung einer ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateModul("1", existingModul);
        });

        // Überprüfen, ob der HTTP-Statuscode der erwarteten UNAUTHORIZED entspricht
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    /**
     * Testet die Validierung eines Moduls mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     * 
     * @return void
     */
    @Test
    public void testValidateModul_ValidInputs() {
        // Erstellen eines Modul-Objekts mit gültigen Eingabewerten
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

        // Validierung des Moduls und Erfassung der Validierungsfehler
        List<String> errors = controller.validateModul(modul);

        // Überprüfen, ob keine Validierungsfehler erwartet werden
        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    /**
     * Testet die Validierung eines Moduls mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param name                  der Name
     * @param workload              die Workload
     * @param credits               die Credits
     * @param dauer                 die Dauer
     * @param art                   die Art
     * @param abschluss             der Abschluss
     * @param beschreibung          die Beschreibung
     * @param lehrveranstaltungsort der Lehrveranstaltungsort
     * @param sprache               die Sprache
     */
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
        // Erstellen eines Modul-Objekts mit den übergebenen ungültigen Eingabewerten
        Modul modul = new Modul();
        modul.setName(name);
        modul.setWorkload(workload != null ? workload : 0); // Setzen der Workload, falls nicht null, sonst 0
        modul.setCredits(credits != null ? credits : 0); // Setzen der Credits, falls nicht null, sonst 0
        modul.setDauer(dauer != null ? dauer : 0); // Setzen der Dauer, falls nicht null, sonst 0
        modul.setArt(art != null ? ModulArt.valueOf(art) : null); // Setzen der ModulArt, falls nicht null und
                                                                  // existiert, sonst null
        modul.setAbschluss(abschluss != null ? Abschluss.valueOf(abschluss) : null); // Setzen des Abschlusses, falls
                                                                                     // nicht null und existiert, sonst
                                                                                     // null
        modul.setBeschreibung(beschreibung); // Setzen der Beschreibung
        modul.setLehrveranstaltungsort(lehrveranstaltungsort); // Setzen des Lehrveranstaltungsorts
        modul.setSprache(sprache != null ? Sprache.valueOf(sprache) : null); // Setzen der Sprache, falls nicht null und
                                                                             // existiert, sonst null

        // Validierung des Moduls und Erfassung der Validierungsfehler
        List<String> errors = controller.validateModul(modul);

        // Überprüfen, ob mindestens ein Validierungsfehler erwartet wird
        assertFalse(errors.isEmpty(), "Es sollte mindestens ein Validierungsfehler auftreten.");
    }
}