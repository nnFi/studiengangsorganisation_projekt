package com.projekt.studiengangsorganisation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.AdminRepository;
import com.projekt.studiengangsorganisation.repository.NutzerRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NutzerRepository nutzerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/register/admin")
    public String registerUser(@RequestBody Admin user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getVorname().toLowerCase() + "." + user.getNachname().toLowerCase());
        adminRepository.save(user);
        return "User registered successfully";
    }

    @GetMapping("/info")
    public Nutzer getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Nutzer nutzer = nutzerRepository.findByUsername(authentication.getName()).get();
        return nutzer;
    }
}
