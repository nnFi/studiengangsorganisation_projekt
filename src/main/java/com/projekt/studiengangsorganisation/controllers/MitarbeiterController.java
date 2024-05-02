package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/mitarbeiter")
@RestController
public class MitarbeiterController {

    @Autowired
    MitarbeiterService mitarbeiterService;

    @GetMapping("/{id}")
    public Mitarbeiter getOne(@RequestParam String id) {
        Optional<Mitarbeiter> mitarbeiter = mitarbeiterService.getMitarbeiter(id);

        if (mitarbeiter.isPresent()) {
            return mitarbeiter.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Mitarbeiter> getAll(HttpServletResponse response) {
        List<Mitarbeiter> list = mitarbeiterService.getMitarbeiter();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}