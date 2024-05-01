package com.projekt.studiengangsorganisation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.service.PruefungService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/pruefung")
@RestController
public class PruefungController {

    @Autowired
    PruefungService pruefungService;

    @GetMapping("")
    public List<Pruefung> getAll(HttpServletResponse response) {
        List<Pruefung> list = pruefungService.getPruefungen();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
