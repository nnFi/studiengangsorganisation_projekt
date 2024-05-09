package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.service.FachbereichService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/fachbereich")
@RestController
public class FachbereichController {

    @Autowired
    FachbereichService fachbereichService;

    @GetMapping("/{id}")
    public Fachbereich getOne(@PathVariable String id) {
        Optional<Fachbereich> fachbereich = fachbereichService.getFachbereich(id);

        if (fachbereich.isPresent()) {
            return fachbereich.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Fachbereich> getAll(HttpServletResponse response) {
        List<Fachbereich> list = fachbereichService.getFachbereiche();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
