package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.repository.StudentRepository;

/**
 * Service-Klasse für die Verwaltung von Studenten.
 */
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    /**
     * Konstruktor für die StudentService-Klasse.
     */
    public StudentService() {

    }

    /**
     * Holt einen Studenten anhand seiner ID.
     * 
     * @param id Die ID des Studenten.
     * @return Ein Optional, das den gefundenen Studenten enthält, falls vorhanden.
     */
    public Optional<Student> getStudent(String id) {
        return studentRepository.findById(Long.parseLong(id));
    }

    /**
     * Holt alle Studenten.
     * 
     * @return Eine Liste aller vorhandenen Studenten.
     */
    public List<Student> getStudenten() {
        return studentRepository.findAll();
    }

    /**
     * Speichert einen Studenten und aktualisiert die Änderungen.
     * 
     * @param student Der Student, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Student.
     */
    public Student saveAndFlush(Student student) {
        return studentRepository.saveAndFlush(student);
    }

    /**
     * Löscht alle Einträge in der Datenbank.
     */
    public void deleteAll() {
        studentRepository.deleteAll();
    }
}