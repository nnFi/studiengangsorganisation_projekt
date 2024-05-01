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

import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/modulgruppe")
@RestController
public class ModulgruppeController {

    @Autowired
    ModulgruppeService modulgruppeService;

    @GetMapping("/{id}")
    public Modulgruppe getOne(@RequestParam String id) {
        Optional<Modulgruppe> modulgruppe = modulgruppeService.getModulgruppe(id);

        if (modulgruppe.isPresent()) {
            return modulgruppe.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Modulgruppe> getAll(HttpServletResponse response) {
        List<Modulgruppe> list = modulgruppeService.getModulgruppen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

}
