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

import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.service.FachgruppeService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/fachgruppe")
@RestController
public class FachgruppeController {

    @Autowired
    FachgruppeService fachgruppeService;

    @GetMapping("/{id}")
    public Fachgruppe getOne(@PathVariable String id) {
        Optional<Fachgruppe> fachgruppe = fachgruppeService.getFachgruppe(id);

        if (fachgruppe.isPresent()) {
            return fachgruppe.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Fachgruppe> getAll(HttpServletResponse response) {
        List<Fachgruppe> list = fachgruppeService.getFachgruppen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
