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

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.service.AdminService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/{id}")
    public Admin getOne(@PathVariable String id) {
        Optional<Admin> admin = adminService.getAdmin(id);

        if (admin.isPresent()) {
            return admin.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<Admin> getAll(HttpServletResponse response) {
        List<Admin> list = adminService.getAdmin();
        response.setHeader("Content-Range", "1-" + list.size());
        return list;
    }
}