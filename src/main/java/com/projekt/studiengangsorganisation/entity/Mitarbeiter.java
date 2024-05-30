package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Entity;

/**
 * Repräsentiert einen Mitarbeiter-Benutzer, welcher von Nutzer erbt.
 * @see Nutzer
 */
@Entity
public class Mitarbeiter extends Nutzer {
    
    /**
     * Standardkonstruktor für die Mitarbeiter-Klasse.
     * Setzt die Rolle auf "MITARBEITER".
     */
    public Mitarbeiter() {
        super();
        this.setRole("MITARBEITER");
    }

}
