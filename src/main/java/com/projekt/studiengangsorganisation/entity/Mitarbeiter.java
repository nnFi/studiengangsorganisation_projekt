package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Mitarbeiter")
public class Mitarbeiter extends Nutzer {
    
    @Id
    @Column(name = "mitarbeiter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
