package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;


@RequestMapping("/modulgruppe")
@RestController
public class ModulgruppeController {

    @Autowired
    ModulgruppeService modulgruppeService;

    @Autowired
    NutzerService nutzerService;

    @GetMapping("/{id}")
    public Modulgruppe getOne(@PathVariable String id) {
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

    @PostMapping("")
    public ResponseEntity<Modulgruppe> createModulgruppe(@RequestBody Modulgruppe modulgruppe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerService.getNutzerByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));

        if (!nutzer.getRole().equals("MITARBEITER") && !nutzer.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        modulgruppeService.saveAndFlush(modulgruppe);

        return new ResponseEntity<>(modulgruppe, HttpStatus.CREATED);
    }
}
