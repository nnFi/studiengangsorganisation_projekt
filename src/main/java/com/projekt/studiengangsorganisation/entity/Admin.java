package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;

/**
 * Repräsentiert einen Admin-Benutzer, welcher von Nutzer erbt.
 * @see Nutzer
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Admin extends Nutzer {

    /**
     * Standardkonstruktor für die Admin-Klasse.
     * Setzt die Rolle auf "ADMIN".
     */
    public Admin() {
        super();
        this.setRole("ADMIN");
    }
}