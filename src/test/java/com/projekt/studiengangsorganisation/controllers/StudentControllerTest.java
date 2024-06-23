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
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.StudentService;
import com.projekt.studiengangsorganisation.service.NutzerService;

/**
 * Testklasse für den StudentController.
 * Verwendet Mockito, um Abhängigkeiten zu mocken und das Verhalten der Methoden
 * zu testen.
 */
public class StudentControllerTest {

    // Mock-Objekte für die Abhängigkeiten der StudentController-Klasse
    // Mock: Simuliert eine Abhängigkeit, kontrolliert Antworten, überprüft
    // Interaktionen
    @Mock
    private StudentService studentService;

    @Mock
    private NutzerService nutzerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentController controller;

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
     * Erwartet, dass der Student zurückgegeben wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_ValidId_ReturnsStudent() {
        // Test für das Abrufen eines Studenten mit gültiger ID
        Student mockStudent = new Student();
        mockStudent.setId(1L);
        when(studentService.getStudent("1")).thenReturn(Optional.of(mockStudent));

        Student result = controller.getOne("1");

        assertEquals(mockStudent, result);
    }

    /**
     * Testet die Methode getOne mit einer ungültigen ID.
     * Erwartet, dass eine ResponseStatusException ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testGetOne_InvalidId_ThrowsResponseStatusException() {
        // Test für das Abrufen eines Studenten mit ungültiger ID
        when(studentService.getStudent("1")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            controller.getOne("1");
        });
    }

    /**
     * Testet die Methode createStudent durch einen Administrator.
     * Erwartet, dass der Student erfolgreich erstellt wird.
     * 
     * @return void
     */
    @Test
    public void testCreateStudent_Administrator_SuccessfullyCreated() {
        // Test für das Erstellen eines Studenten durch einen Administrator
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Setzt den Sicherheitskontext, um den Administrator-Benutzer zurückzugeben
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocking des Studenten
        Student mockStudent = new Student();
        mockStudent.setVorname("Muster");
        mockStudent.setNachname("Student");
        mockStudent.setPassword("Secure!Pass123");

        // Mocking der Passwortkodierung
        when(passwordEncoder.encode(mockStudent.getPassword())).thenReturn("Secure!Pass123");

        // Mocking der saveAndFlush-Methode
        when(studentService.saveAndFlush(any(Student.class))).thenReturn(mockStudent);

        // Ausführen des createStudent-Aufrufs
        ResponseEntity<Student> response = controller.createStudent(mockStudent);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Testet die Methode createStudent durch einen Nicht-Administrator.
     * Erwartet, dass der Student nicht erstellt wird und eine Ausnahme
     * ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testCreateStudent_NonAdministrator_NotCreated() {
        // Test für das Erstellen eines Studenten durch einen Nicht-Administrator
        Nutzer user = new Student();
        user.setUsername("max.mustermann");
        user.setRole("STUDENT");

        // Setzt den Sicherheitskontext, um den Nicht-Administrator-Benutzer
        // zurückzugeben
        when(authentication.getName()).thenReturn("max.mustermann");
        when(nutzerService.getNutzerByUsername("max.mustermann")).thenReturn(Optional.of(user));

        // Mocking des Studenten
        Student mockStudent = new Student();
        mockStudent.setVorname("Muster");
        mockStudent.setNachname("Student");
        mockStudent.setPassword("Secure!Pass123");

        // Ausführen des createStudent-Aufrufs und Erwarten einer Ausnahme
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createStudent(mockStudent);
        });

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    /**
     * Testet die Methode updateStudent durch einen Administrator.
     * Erwartet, dass der Student erfolgreich aktualisiert wird.
     * 
     * @return void
     */
    @SuppressWarnings("null")
    @Test
    public void testUpdateStudent_Administrator_SuccessfullyUpdated() {
        // Test für das Aktualisieren eines Studenten durch einen Administrator
        Nutzer admin = new Admin();
        admin.setUsername("test.admin");
        admin.setRole("ADMIN");

        // Setzt den Sicherheitskontext, um den Administrator-Benutzer zurückzugeben
        when(authentication.getName()).thenReturn("test.admin");
        when(nutzerService.getNutzerByUsername("test.admin")).thenReturn(Optional.of(admin));

        // Mocking des existierenden Studenten
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setVorname("Muster");
        existingStudent.setNachname("Student");

        // Mocking des aktualisierten Studenten
        Student updatedStudent = new Student();
        updatedStudent.setVorname("Musterin");
        updatedStudent.setNachname("Studentin");
        updatedStudent.setPassword("Secure!Pass123");

        // Mocking der getStudent-Methode
        when(studentService.getStudent("1")).thenReturn(Optional.of(existingStudent));

        // Ausführen des updateStudent-Aufrufs
        ResponseEntity<Student> response = controller.updateStudent("1", updatedStudent);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Musterin", response.getBody().getVorname());
        assertEquals("Studentin", response.getBody().getNachname());
    }

    /**
     * Testet die Methode updateStudent durch einen Nicht-Administrator.
     * Erwartet, dass der Student nicht aktualisiert wird und eine Ausnahme
     * ausgelöst wird.
     * 
     * @return void
     */
    @Test
    public void testUpdateStudent_NonAdministrator_NotUpdated() {
        // Test für das Aktualisieren eines Studenten durch einen Nicht-Administrator
        Nutzer user = new Student();
        user.setUsername("max.mustermann");
        user.setRole("STUDENT");

        // Setzt den Sicherheitskontext, um den Nicht-Administrator-Benutzer
        // zurückzugeben
        when(authentication.getName()).thenReturn("max.mustermann");
        when(nutzerService.getNutzerByUsername("max.mustermann")).thenReturn(Optional.of(user));

        // Mocking des existierenden Studenten
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setVorname("Muster");
        existingStudent.setNachname("Student");

        // Mocking des aktualisierten Studenten
        Student updatedStudent = new Student();
        updatedStudent.setVorname("Musterin");
        updatedStudent.setNachname("Studentin");
        updatedStudent.setPassword("Secure!Pass123");

        // Mocking der getStudent-Methode
        when(studentService.getStudent("1")).thenReturn(Optional.of(existingStudent));

        // Ausführen des updateStudent-Aufrufs und Erwarten einer Ausnahme
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateStudent("1", updatedStudent);
        });

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    /**
     * Testet die Validierung eines Studenten mit gültigen Eingaben.
     * Erwartet, dass keine Validierungsfehler auftreten.
     *
     * @param vorname  der Vorname des Studenten
     * @param nachname der Nachname des Studenten
     * @param password das Passwort des Studenten
     */
    @ParameterizedTest
    @CsvSource({
            "Johann, Schneider, Valid1!password",
            "Alicia, Schmidt, AnotherValid1Passwo-rd",
            "Emilia, Becker, Secure!Pass123",
    })
    public void testValidateStudent_ValidInputs(String vorname, String nachname, String password) {
        // Test für die Validierung eines Studenten mit gültigen Eingaben
        vorname = vorname != null ? vorname : "";
        nachname = nachname != null ? nachname : "";
        password = password != null ? password : "";

        Student student = new Student();
        student.setVorname(vorname);
        student.setNachname(nachname);
        student.setPassword(password);

        List<String> errors = controller.validateStudent(student);

        assertTrue(errors.isEmpty(), "Es sollte keine Validierungsfehler auftreten.");
    }

    /**
     * Testet die Validierung eines Studenten mit ungültigen Eingaben.
     * Erwartet, dass Validierungsfehler auftreten.
     *
     * @param vorname  der Vorname des Studenten
     * @param nachname der Nachname des Studenten
     * @param password das Passwort des Studenten
     */
    @ParameterizedTest
    @CsvSource({
            ", Schmidt, Valid1!password", // Leerzeichen als Vorname
            "Johann, , Valid1!password", // Leerzeichen als Nachname
            ", , Valid1!password", // Leerzeichen als Vor- und Nachname
            "Johann, Schmidt, ", // Leerzeichen als Passwort
            "Johann, Schmidt, 123", // Passwort zu kurz
            "Johann, Schmidt, invalid", // Passwort enthält kein Sonderzeichen
            "Johann, Schmidt, invalidpassword", // Passwort enthält kein Großbuchstaben
            "Johann, Schmidt, invalidPassword", // Passwort enthält keine Zahl
            "Johann, Schmidt, INVALID!", // Passwort enthält kein Kleinbuchstaben
    })
    public void testValidateStudent_InvalidInputs(String vorname, String nachname, String password) {
        // Test für die Validierung eines Studenten mit ungültigen Eingaben
        vorname = vorname != null ? vorname : "";
        nachname = nachname != null ? nachname : "";
        password = password != null ? password : "";

        Student student = new Student();
        student.setVorname(vorname);
        student.setNachname(nachname);
        student.setPassword(password);

        List<String> errors = controller.validateStudent(student);

        assertTrue(!errors.isEmpty(), "Es sollte ein Validierungsfehler auftreten.");
    }
}