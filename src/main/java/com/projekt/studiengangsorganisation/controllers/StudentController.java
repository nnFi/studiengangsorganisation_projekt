package com.projekt.studiengangsorganisation.controllers;

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
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller-Klasse für die Verwaltung von Studenten.
 */
@RequestMapping("/student")
@RestController
public class StudentController {

    // Encoder für Passwörter
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Service für Studenten
    @Autowired
    StudentService studentService; 

    // Service für Nutzer
    @Autowired
    NutzerService nutzerService;

    /**
     * Liefert einen Studenten basierend auf der ID.
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
     * Liefert alle Studenten.
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
     * Erstellt einen neuen Studenten.
     * @param student Die zu erstellende Studentenentität.
     * @return Die erstellte Studentenentität.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist oder nicht gefunden wird.
     */
    @PostMapping("")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());

        if (nutzer.isPresent() && nutzer.get().getRole().equals("ADMIN")) {
            // Falls der Benutzer ein ADMIN ist
            student.setPassword(passwordEncoder.encode(student.getPassword())); // Passwort kodieren
            student.setUsername(student.getVorname().toLowerCase() + "." + student.getNachname().toLowerCase()); // Benutzername erstellen

            // Student speichern
            studentService.saveAndFlush(student);

            // Erfolgreiche Erstellung
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        } else {
            // Andernfalls: Nicht gefunden Fehler
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Aktualisiert einen vorhandenen Studenten.
     * @param id             Die ID des zu aktualisierenden Studenten.
     * @param updatedStudent Die aktualisierte Studentenentität.
     * @return Die aktualisierte Studentenentität.
     * @throws ResponseStatusException Falls der Benutzer nicht autorisiert ist, der Student nicht gefunden wird oder die Aktualisierung fehlschlägt.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateMitarbeiter(@PathVariable String id, @RequestBody Student updatedStudent) {
        Optional<Student> existingStudent = studentService.getStudent(id);

        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();

            // Überprüfe, ob der Benutzer ein ADMIN ist
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());
            if (!nutzer.isPresent() || !nutzer.get().getRole().equals("ADMIN")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nur Administratoren können Mitarbeiter aktualisieren");
            }

            // Aktualisiere die Felder des Mitarbeiters
            student.setVorname(updatedStudent.getVorname());
            student.setNachname(updatedStudent.getNachname());
            student.setUsername(updatedStudent.getVorname().toLowerCase() + "." + updatedStudent.getNachname().toLowerCase());

            // Speichere die aktualisierten Mitarbeiterdaten
            studentService.saveAndFlush(student);

            return new ResponseEntity<>(student, HttpStatus.OK); // Erfolgreiche Aktualisierung
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nicht gefunden"); // Andernfalls: Nicht gefunden Fehler
        }
    }
}