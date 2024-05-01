package com.projekt.studiengangsorganisation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.service.NutzerService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/nutzer")
@RestController
public class NutzerController {

    @Autowired
    NutzerService nutzerService;

    @GetMapping("")
    public List<Nutzer> getAll(HttpServletResponse response) {
        List<Nutzer> list = nutzerService.getNutzer();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}
