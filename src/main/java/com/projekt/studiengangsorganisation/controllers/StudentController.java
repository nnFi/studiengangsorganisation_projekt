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

@RequestMapping("/student")
@RestController
public class StudentController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    StudentService studentService;

    @Autowired
    NutzerService nutzerService;

    @GetMapping("/{id}")
    public Student getOne(@PathVariable String id) {
        Optional<Student> student = studentService.getStudent(id);

        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Student> getAll(HttpServletResponse response) {
        List<Student> list = studentService.getStudenten();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

    @PostMapping("")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Nutzer> nutzer = nutzerService.getNutzerByUsername(authentication.getName());

        if (nutzer.isPresent() && nutzer.get().getRole().equals("ADMIN")) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            student.setUsername(
                    student.getVorname().toLowerCase() + "." + student.getNachname().toLowerCase());

            studentService.saveAndFlush(student);

            return new ResponseEntity<>(student, HttpStatus.CREATED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

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

            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nicht gefunden");
        }
    }
}
