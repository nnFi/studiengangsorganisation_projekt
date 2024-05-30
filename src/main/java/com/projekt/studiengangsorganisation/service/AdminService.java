package com.projekt.studiengangsorganisation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.repository.AdminRepository;

/**
 * Service-Klasse für die Verwaltung von Admin-Daten.
 */
@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    /**
     * Konstruktor für die AdminService-Klasse.
     */
    public AdminService() {

    }

    /**
     * Holt einen Admin anhand seiner ID.
     * @param id Die ID des Admins.
     * @return Ein Optional, das den gefundenen Admin enthält, falls vorhanden.
     */
    public Optional<Admin> getAdmin(String id) {
        return adminRepository.findById(Long.parseLong(id));
    }

    /**
     * Holt alle Admins.
     * @return Eine Liste aller vorhandenen Admins.
     */
    public List<Admin> getAdmin() {
        return adminRepository.findAll();
    }

    /**
     * Speichert einen Admin und aktualisiert die Änderungen.
     * @param admin Der Admin, der gespeichert werden soll.
     * @return Der gespeicherte und aktualisierte Admin.
     */
    public Admin saveAndFlush(Admin admin) {
        return adminRepository.saveAndFlush(admin);
    }

    /**
     * Fügt Testdaten für einen Admin ein.
     * @return Der erstellte Admin mit Testdaten.
     */
    public Admin insertTestData() {
        Admin admin = new Admin();
        admin.setVorname("Max");
        admin.setNachname("Mustermann");

        adminRepository.saveAndFlush(admin);

        return admin;
    }
}