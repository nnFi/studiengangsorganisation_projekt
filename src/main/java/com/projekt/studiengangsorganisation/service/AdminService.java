package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public AdminService() {

    }

    public Optional<Admin> getAdmin(String id) {
        return adminRepository.findById(Long.parseLong(id));
    }

    public List<Admin> getAdmin() {
        return adminRepository.findAll();
    }

    public Admin insertTestData() {
        Admin admin = new Admin();
        admin.setVorname("Max");
        admin.setNachname("Mustermann");

        adminRepository.saveAndFlush(admin);

        return admin;
    }
}
