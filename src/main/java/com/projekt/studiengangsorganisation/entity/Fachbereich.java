package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fachbereich")
public class Fachbereich{

    @Id
    @Column(name = "fachbereich_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fachbereich_referent")
    private Nutzer referent;

    @Column(name = "fachbereich_stellvertreter")
    private Nutzer stellvertreter;
}
