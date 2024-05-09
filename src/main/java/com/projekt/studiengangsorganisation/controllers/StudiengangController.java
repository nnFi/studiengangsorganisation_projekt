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

import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.service.StudiengangService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/studiengang")
@RestController
public class StudiengangController {

    @Autowired
    StudiengangService studiengangService;

    @GetMapping("/{id}")
    public Studiengang getOne(@PathVariable String id) {
        Optional<Studiengang> studiengang = studiengangService.getStudiengang(id);

        if (studiengang.isPresent()) {
            return studiengang.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Studiengang> getAll(HttpServletResponse response) {
        List<Studiengang> list = studiengangService.getStudiengaenge();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
