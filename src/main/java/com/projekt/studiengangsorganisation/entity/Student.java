package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;

/**
 * Repräsentiert einen Student-Benutzer, welcher von Nutzer erbt.
 * @see Nutzer
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student extends Nutzer {

    /**
     * Standardkonstruktor für die Student-Klasse.
     * Setzt die Rolle auf "STUDENT".
     */
    public Student() {
        super();
        this.setRole("STUDENT");
    }

}
