package com.projekt.studiengangsorganisation.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.security.PasswordValidator;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für die Verwaltung von Studenten.
 */
@RequestMapping("/api/student")
@RestController
public class StudentController {

    // Encoder für Passwörter
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Deklarierung Services
    @Autowired
    StudentService studentService;

    @Autowired
    NutzerService nutzerService;

    /**
     * Methode zum Abrufen eines Studenten basierend auf der ID.
     * 
     * @param id Die ID des Studenten.
     * @return Der gefundene Student.
     * @throws ResponseStatusException Falls der Student nicht gefunden wird.
     */
    @GetMapping("/{id}")
    public Student getOne(@PathVariable String id) {
        Optional<Student> student = studentService.getStudent(id);

        if (student.isPresent()) {
            return student.get(); // Falls vorhanden, gib den Studenten zurück
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // Andernfalls: Nicht gefunden Fehler
        }
    }

    /**
     * Methode zum Abrufen aller Studenten.
     * 
     * @param response Die HTTP-Response.
     * @return Eine Liste aller Studenten.
     */
    @GetMapping("")
    public List<Student> getAll(HttpServletResponse response) {
        List<Student> list = studentService.getStudenten();
        // Setze Content-Range Header
        response.setHeader("Content-Range", "1-" + list.size());
        // Gib die Liste der Studenten zurück
        return list;
    }

    /**
     * Methode zum Erstellen eines neuen Studenten.
     * 
     * @param student Die zu erstellende Studentenentität.
     * @return Die erstellte Studentenentität.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist oder
     *                                 nicht gefunden wird.
     */
    @PostMapping("")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());

        if (nutzer.isPresent() && nutzer.get().getRole().equals("ADMIN")) {
            // Falls der Benutzer ein ADMIN ist
            student.setPassword(passwordEncoder.encode(student.getPassword())); // Passwort kodieren
            student.setUsername(student.getVorname().toLowerCase() + "." + student.getNachname().toLowerCase()); // Benutzername
                                                                                                                 // erstellen
            // Validierungslogik für die Eingabefelder
            List<String> errors = validateStudent(student);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            // Student speichern
            studentService.saveAndFlush(student);

            // Erfolgreiche Erstellung
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        } else {
            // Andernfalls: Nicht gefunden Fehler
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nur Administratoren können Studenten erstellen");
        }
    }

    /**
     * Methode zum Aktualisieren eines vorhandenen Studenten.
     * 
     * @param id             Die ID des zu aktualisierenden Studenten.
     * @param updatedStudent Die aktualisierte Studentenentität.
     * @return Die aktualisierte Studentenentität.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist, der
     *                                 Student nicht gefunden wird oder die
     *                                 Aktualisierung fehlschlägt.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        Optional<Student> existingStudent = studentService.getStudent(id);

        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();

            // Überprüfe, ob der Benutzer ein ADMIN ist
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());
            if (!nutzer.isPresent() || !nutzer.get().getRole().equals("ADMIN")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Nur Administratoren können Studenten aktualisieren");
            }

            // Validierungslogik für die Eingabefelder
            List<String> errors = validateStudent(updatedStudent);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
            }

            // Aktualisiere die Felder des Studentens
            student.setVorname(updatedStudent.getVorname());
            student.setNachname(updatedStudent.getNachname());
            student.setUsername(
                    (updatedStudent.getVorname().toLowerCase() + "." + updatedStudent.getNachname().toLowerCase())
                            .replace("ß", "ss"));

            // Speichere die aktualisierten Studentendaten
            studentService.saveAndFlush(student);

            return new ResponseEntity<>(student, HttpStatus.OK); // Erfolgreiche Aktualisierung
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nicht gefunden"); // Andernfalls: Nicht
                                                                                               // gefunden Fehler
        }
    }

    /**
     * Validiert das übergebene Mitarbeiter-Objekt.
     * 
     * @param student das zu validierende Student-Objekt
     * @return eine Liste von Fehlermeldungen, leer wenn keine Validierungsfehler
     *         vorliegen
     */
    List<String> validateStudent(Student student) {
        List<String> errors = new ArrayList<>();

        // Überprüfung erforderlicher Felder
        if (student.getVorname() == null || student.getVorname().isEmpty()) {
            errors.add("Das Feld 'Vorname' ist erforderlich.");
        }

        if (student.getNachname() == null || student.getNachname().isEmpty()) {
            errors.add("Das Feld 'Nachname' ist erforderlich.");
        }

        // Längenprüfung
        if (student.getVorname() != null && student.getVorname().length() < 2) {
            errors.add("Das Feld 'Vorname' muss mindestens 2 Zeichen lang sein.");
        }

        if (student.getNachname() != null && student.getNachname().length() < 2) {
            errors.add("Das Feld 'Nachname' muss mindestens 2 Zeichen lang sein.");
        }

        // Benutzername-Formatprüfung
        String username = (student.getVorname().toLowerCase() + "." + student.getNachname().toLowerCase()).replace("ß",
                "ss");
        if (!username.matches("^[a-z0-9]+\\.[a-z0-9]+$")) {
            errors.add("Das Feld 'Username' hat ein ungültiges Format.");
        }

        // Passwort prüfen
        if (PasswordValidator.validate(student.getPassword())) {
            errors.add(
                    "Passwort entspricht nicht den Anforderungen. (Groß- und Kleinbuchstaben, Sonderzeichen, Zahlen, Mindeslänge 8)");
        }

        return errors;
    }
}