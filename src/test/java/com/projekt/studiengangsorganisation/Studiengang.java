package com.projekt.studiengangsorganisation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="studiengang")
public class Studiengang {


    @Id
    @Column(name="studiengang_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="studiengang_name")
    private String name;

    @Column(name="studiengang_abschluss")
    private String abschluss;

    @Column(name="studiengang_regelstudienzeit")
    private int regelstudienzeit;

    @Column(name="studiengang_studiengangsleiter")
    private Nutzer studiengangsLeiter;
    
}