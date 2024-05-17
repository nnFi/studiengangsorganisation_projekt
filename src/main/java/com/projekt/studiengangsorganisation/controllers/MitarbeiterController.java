package com.projekt.studiengangsorganisation.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.MitarbeiterRepository;
import com.projekt.studiengangsorganisation.repository.NutzerRepository;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/mitarbeiter")
@RestController
public class MitarbeiterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    NutzerRepository nutzerRepository;

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    @GetMapping("/{id}")
    public Mitarbeiter getOne(@PathVariable String id) {
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

    @PostMapping("")
    public ResponseEntity<Mitarbeiter> createMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Nutzer> nutzer = nutzerRepository.findByUsername(authentication.getName());

        if (nutzer.isPresent() && nutzer.get().getRole().equals("ADMIN")) {
            mitarbeiter.setPassword(passwordEncoder.encode(mitarbeiter.getPassword()));
            mitarbeiter.setUsername(
                    mitarbeiter.getVorname().toLowerCase() + "." + mitarbeiter.getNachname().toLowerCase());

            mitarbeiterRepository.saveAndFlush(mitarbeiter);

            return new ResponseEntity<>(mitarbeiter, HttpStatus.CREATED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}