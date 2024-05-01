package com.projekt.studiengangsorganisation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/mitarbeiter")
@RestController
public class NutzerController {

    @Autowired
    MitarbeiterService mitarbeiterService;

    @GetMapping("")
    public List<Mitarbeiter> getAll(HttpServletResponse response) {
        List<Mitarbeiter> list = mitarbeiterService.getMitarbeiter();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}