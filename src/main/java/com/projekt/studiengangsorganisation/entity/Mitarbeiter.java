package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Entity;

@Entity
public class Mitarbeiter extends Nutzer {

    public Mitarbeiter() {
        super();
        this.setRole("MITARBEITER");
    }

}
