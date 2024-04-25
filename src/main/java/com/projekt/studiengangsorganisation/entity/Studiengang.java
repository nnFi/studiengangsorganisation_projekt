package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name="leiter_id")
    private Mitarbeiter leiter;

    @ManyToOne
    @JoinColumn(name="stellvertretenderLeiter_id")
    private Mitarbeiter stellvertretenderLeiter;
    
    @ManyToOne
    @JoinColumn(name="fachbereich_id")
    private Fachbereich fachbereich;

    @ManyToOne
    @JoinColumn(name="pruefungsordnung_id")
    private Pruefungsordnung pruefungsordung;

}