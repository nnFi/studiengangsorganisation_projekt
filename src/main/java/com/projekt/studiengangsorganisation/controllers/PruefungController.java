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

import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.service.PruefungService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/pruefung")
@RestController
public class PruefungController {

    @Autowired
    PruefungService pruefungService;

    @GetMapping("/{id}")
    public Pruefung getOne(@PathVariable String id) {
        Optional<Pruefung> pruefung = pruefungService.getPruefung(id);

        if (pruefung.isPresent()) {
            return pruefung.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Pruefung> getAll(HttpServletResponse response) {
        List<Pruefung> list = pruefungService.getPruefungen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
