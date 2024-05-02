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

import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/pruefungsordnung")
@RestController
public class PruefungsordnungController {
    
    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @GetMapping("/{id}")
    public Pruefungsordnung getOne(@PathVariable String id) {
        Optional<Pruefungsordnung> pruefungsordnung = pruefungsordnungService.getPruefungsordnung(id);

        if (pruefungsordnung.isPresent()) {
            return pruefungsordnung.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Pruefungsordnung> getAll(HttpServletResponse response) {
        List<Pruefungsordnung> list = pruefungsordnungService.getPruefungsordnungen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
