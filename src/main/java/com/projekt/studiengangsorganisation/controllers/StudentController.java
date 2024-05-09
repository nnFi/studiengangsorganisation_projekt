package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/student")
@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

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
}