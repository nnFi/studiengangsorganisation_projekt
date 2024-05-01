package com.projekt.studiengangsorganisation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public StudentService() {

    }
    
    public List<Student> getStudenten () {
        return studentRepository.findAll();
    }

    public void insertTestData() {
        Student student = new Student();
        student.setVorname("Max");
        student.setNachname("Mustermann");

        studentRepository.save(student);
    }
}
