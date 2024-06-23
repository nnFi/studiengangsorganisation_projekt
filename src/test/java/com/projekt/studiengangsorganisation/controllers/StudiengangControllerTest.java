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

    // Mock-Objekte für die Abhängigkeiten der MitarbeiterController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antwrten, überprüft
    // Interaktionen
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
     * Erwartet, dass der Studiengang zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsStudiengang() {
        // Erstellen von Mock-Mitarbeiterobjekten
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);
    
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);
    
        // Erstellen eines Mock-Fachbereichsobjekts
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
    
        // Erstellen eines Mock-Studiengangsobjekts mit den oben erstellten Mitarbeitern und Fachbereich
        Studiengang studiengang = new Studiengang();
        studiengang.setId(1L);
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);
    
        // Mocken des Verhaltens des StudiengangService, sodass das oben erstellte Studiengangsobjekt zurückgegeben wird, wenn die ID 1 ist
        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(studiengang));
    
        // Aufrufen der getOne Methode des Controllers mit der ID 1 und speichern des Ergebnisses
        Studiengang result = controller.getOne("1");
    
        // Assertions
        assertEquals(studiengang.getId(), result.getId());
        assertEquals(studiengang.getName(), result.getName());
        assertEquals(studiengang.getAbschluss(), result.getAbschluss());
        assertEquals(studiengang.getRegelstudienzeit(), result.getRegelstudienzeit());
    }

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Mocken des Verhaltens des StudiengangService, sodass ein leeres Optional zurückgegeben wird, wenn die ID 1 ist
    when(studiengangService.getStudiengang(1L)).thenReturn(Optional.empty());

        // Überprüfen, ob eine ResponseStatusException geworfen wird, wenn die getOne Methode des Controllers mit der ID 1 aufgerufen wird
        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode getAll.
     * Erwartet, dass alle Studiengaenge zurückgegeben werden.
     * 
     * @return void
     */
    @Test
    public void testGetAll_ReturnsAllStudiengaenge() {
        // Erstellen von Mock-Mitarbeiterobjekten
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        // Erstellen eines Mock-Fachbereichsobjekts
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);

        // Erstellen von zwei Mock-Studiengangsobjekten
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

        // Erstellen einer Liste von Studiengangsobjekten
        List<Studiengang> studiengaenge = Arrays.asList(studiengang1, studiengang2);

        // Mocken des Verhaltens des StudiengangService, sodass die Liste der Studiengänge zurückgegeben wird
        when(studiengangService.getStudiengaenge()).thenReturn(studiengaenge);

        // Mocken des HttpServletResponse-Objekts
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Aufrufen der getAll Methode des Controllers und Speichern des Ergebnisses
        List<Studiengang> result = controller.getAll(response);

        // Assertions
        assertEquals(2, result.size());
    }

    /**
     * Testet die Methode createStudiengang durch einen Administrator.
     * Erwartet, dass der Studiengang erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateStudiengang_Administrator_SuccessfullyCreated() {        
        // Erstellen eines Mock-Administratornutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Mocken des Verhaltens der Authentication und NutzerService
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Erstellen von Mock-Mitarbeiterobjekten
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);

        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);

        // Erstellen eines Mock-Fachbereichsobjekts
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);

        // Erstellen eines Mock-Studiengangsobjekts
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiterId(1L);
        studiengang.setStellvertreterId(2L);
        studiengang.setFachbereichId(1L);

        // Mocken des Verhaltens der MitarbeiterService und FachbereichService
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(leiter));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));
        when(studiengangService.saveAndFlush(any(Studiengang.class))).thenReturn(studiengang);

        // Aufrufen der createStudiengang Methode des Controllers und Speichern des Ergebnisses
        ResponseEntity<Studiengang> response = controller.createStudiengang(studiengang);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(studiengang, response.getBody());
    }

    /**
     * Testet die Methode createStudiengang durch einen Studenten.
     * Erwartet, dass der Studiengang nicht erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateStudiengang_Student_NotAuthorizedUser() {
        // Erstellen eines Mock-Studenten-Nutzers
        Nutzer student = new Student();
        student.setUsername("test.student");
        student.setRole("STUDENT");
    
        // Mocken des Verhaltens der Authentication und NutzerService
        // Hier wird angenommen, dass der aktuelle authentifizierte Nutzer "test.student" ist
        when(authentication.getName()).thenReturn("test.student");
        when(nutzerService.getNutzerByUsername("test.student")).thenReturn(Optional.of(student));
    
        // Erstellen von Mock-Mitarbeiterobjekten für den Leiter und stellvertretenden Leiter
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);
    
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);
    
        // Erstellen eines Mock-Fachbereichsobjekts und Setzen der Referenten und Stellvertreter
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);
    
        // Erstellen eines Mock-Studiengangsobjekts und Setzen der notwendigen Felder
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiterId(1L);
        studiengang.setStellvertreterId(2L);
        studiengang.setFachbereichId(1L);
    
        // Mocken des Verhaltens der MitarbeiterService und FachbereichService
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(leiter));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));
    
        // Aufrufen der createStudiengang Methode des Controllers und Überprüfen, ob eine Ausnahme geworfen wird
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> {
            controller.createStudiengang(studiengang);
        });
    
        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    /**
     * Testet die Methode updateStudiengang durch einen Administrator.
     * Erwartet, dass der Studiengang erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @SuppressWarnings("null")
    @Test
    public void testUpdateStudiengang_Administrator_SuccessfullyUpdated() {
        // Erstellen eines Mock-Administratornutzers
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");
    
        // Mocken des Verhaltens der Authentication und NutzerService
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));
    
        // Erstellen von Mock-Mitarbeiterobjekten für den Leiter und stellvertretenden Leiter
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);
    
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);
    
        // Erstellen eines Mock-Fachbereichsobjekts und Setzen der Referenten und Stellvertreter
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);
    
        // Erstellen eines Mock-Studiengangsobjekts für den vorhandenen Studiengang
        Studiengang existingStudiengang = new Studiengang();
        existingStudiengang.setId(1L);
        existingStudiengang.setName("Informatik");
        existingStudiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        existingStudiengang.setRegelstudienzeit(6);
        existingStudiengang.setLeiter(leiter);
        existingStudiengang.setStellvertretenderLeiter(stellvertreter);
        existingStudiengang.setFachbereich(fachbereich);
    
        // Erstellen eines Mock-Studiengangsobjekts für den aktualisierten Studiengang
        Studiengang updatedStudiengang = new Studiengang();
        updatedStudiengang.setId(1L);
        updatedStudiengang.setName("Informatik");
        updatedStudiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        updatedStudiengang.setRegelstudienzeit(6);
        updatedStudiengang.setLeiterId(2L); // Neuer Leiter
        updatedStudiengang.setStellvertreterId(1L); // Neuer stellvertretender Leiter
        updatedStudiengang.setFachbereichId(1L);
    
        // Mocken des Verhaltens der StudiengangService, MitarbeiterService und FachbereichService
        when(studiengangService.getStudiengang(1L)).thenReturn(Optional.of(existingStudiengang));
        when(mitarbeiterService.getMitarbeiter(1L)).thenReturn(Optional.of(leiter));
        when(mitarbeiterService.getMitarbeiter(2L)).thenReturn(Optional.of(stellvertreter));
        when(fachbereichService.getFachbereich(1L)).thenReturn(Optional.of(fachbereich));
    
        // Mocken des Speicherns und Aktualisierens des Studiengangs
        when(studiengangService.saveAndFlush(any(Studiengang.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Gibt das gespeicherte Objekt zurück
    
        // Aufrufen der updateStudiengang Methode des Controllers und Speichern des Ergebnisses
        ResponseEntity<Studiengang> response = controller.updateStudiengang("1", updatedStudiengang);
    
        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStudiengang.getName(), response.getBody().getName());
        assertEquals(updatedStudiengang.getAbschluss(), response.getBody().getAbschluss());
        assertEquals(updatedStudiengang.getRegelstudienzeit(), response.getBody().getRegelstudienzeit());
    }

    /**
     * Testet die Methode updateStudiengang durch einen nicht-Admin-Nutzer.
     * Erwartet, dass der Studiengang nicht aktualisiert wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateStudiengang_NotAuthorized_ThrowsResponseStatusException() {
        // Erstellen eines Mock-Studenten-Nutzers
        Nutzer user = new Student();
        user.setUsername("test.user");
        user.setRole("STUDENT");
    
        // Mocken des Verhaltens der Authentication und NutzerService
        // Hier wird angenommen, dass der aktuelle authentifizierte Nutzer "test.user" ist
        when(authentication.getName()).thenReturn("test.user");
        when(nutzerService.getNutzerByUsername("test.user")).thenReturn(Optional.of(user));
    
        // Erstellen eines Mock-Studiengangsobjekts für den vorhandenen Studiengang
        Studiengang existingStudiengang = new Studiengang();
        existingStudiengang.setId(1L);
        existingStudiengang.setName("Informatik");
    
        // Aufrufen der updateStudiengang Methode des Controllers und Überprüfen, ob eine Ausnahme geworfen wird
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateStudiengang("1", existingStudiengang);
        });
    
        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    /**
     * Testet die Validierung eines Fachbereichs mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     * 
     * @return void
     */
    @Test
    public void testValidateStudiengang_ValidInputs() {
        // Erstellen von Mock-Mitarbeiterobjekten für den Leiter und stellvertretenden Leiter
        Mitarbeiter leiter = new Mitarbeiter();
        leiter.setId(1L);
    
        Mitarbeiter stellvertreter = new Mitarbeiter();
        stellvertreter.setId(2L);
    
        // Erstellen eines Mock-Fachbereichsobjekts und Setzen der Referenten und Stellvertreter
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setId(1L);
        fachbereich.setReferent(leiter);
        fachbereich.setStellvertreter(stellvertreter);
    
        // Erstellen eines Mock-Studiengangsobjekts und Setzen der notwendigen Felder
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Informatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);
    
        // Aufrufen der validateStudiengang Methode des Controllers und Speichern der Fehlerliste
        List<String> errors = controller.validateStudiengang(studiengang);
    
        // Assertions
        assertTrue(errors.isEmpty(), "Es sollten keine Fehler auftreten.");
    }

    /**
     * Testet die Validierung eines Fachbereichs mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param name  der Name des Fachbereichs
     * @param abschluss der Abschluss
     * @param regelstudienzeit die Regelstudienzeit
     * @param leiterId der ID des Leiters
     * @param stellvertreterId der ID des Stellvertreters
     * @param fachbereichId der ID des Fachbereichs
     */
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
    
        // Initialisierung von Mitarbeitern und Fachbereich als null
        Mitarbeiter leiter = null;
        Mitarbeiter stellvertreter = null;
        Fachbereich fachbereich = null;

        // Falls eine leiterId übergeben wird, wird ein Mitarbeiter-Objekt erstellt und gemockt
        if (leiterId != null) {
            leiter = new Mitarbeiter();
            leiter.setId(leiterId);
            when(mitarbeiterService.getMitarbeiter(leiterId)).thenReturn(Optional.of(leiter));
        }

        // Falls eine stellvertreterId übergeben wird, wird ein Mitarbeiter-Objekt erstellt und gemockt
        if (stellvertreterId != null) {
            stellvertreter = new Mitarbeiter();
            stellvertreter.setId(stellvertreterId);
            when(mitarbeiterService.getMitarbeiter(stellvertreterId)).thenReturn(Optional.of(stellvertreter));
        }

        // Falls eine fachbereichId übergeben wird, wird ein Fachbereich-Objekt erstellt und gemockt
        if (fachbereichId != null) {
            fachbereich = new Fachbereich();
            fachbereich.setId(fachbereichId);
            when(fachbereichService.getFachbereich(fachbereichId)).thenReturn(Optional.of(fachbereich));
        }

        // Erstellen eines Studiengang-Objekts mit den übergebenen Parametern
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

        // Aufrufen der validateStudiengang Methode des Controllers und Speichern der Fehlerliste
        List<String> errors = controller.validateStudiengang(studiengang);

        // Überprüfen, ob die Fehlerliste nicht leer ist (es sollte mindestens ein Validierungsfehler auftreten)
        assertFalse(errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}