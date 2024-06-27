package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Repräsentiert eine Modulgruppe.
 * 
 * @author Erkan Yüzer
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "modulgruppe")
public class Modulgruppe {

    /**
     * Die eindeutige ID der Modulgruppe.
     */
    @Id
    @Column(name = "modulgruppe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Der Name der Modulgruppe.
     */
    @Column(name = "name")
    private String name;

    /**
     * Gibt die ID der Modulgruppe zurück.
     * 
     * @return die ID der Modulgruppe.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID der Modulgruppe.
     * 
     * @param id die ID der Modulgruppe.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Modulgruppe zurück.
     * 
     * @return der Name der Modulgruppe.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Modulgruppe.
     * 
     * @param name der Name der Modulgruppe.
     */
    public void setName(String name) {
        this.name = name;
    }
}