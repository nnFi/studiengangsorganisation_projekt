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

import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.service.ModulService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/modul")
@RestController
public class ModulController {

    @Autowired
    ModulService modulService;

    @GetMapping("/{id}")
    public Modul getOne(@RequestParam String id) {
        Optional<Modul> modul = modulService.getModul(id);

        if (modul.isPresent()) {
            return modul.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Modul> getAll(HttpServletResponse response) {
        List<Modul> list = modulService.getModule();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }

}
