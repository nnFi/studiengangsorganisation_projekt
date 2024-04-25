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
@Table(name = "fachgruppe")
public class Fachgruppe {

    @Id
    @Column(name = "fachgruppe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "referent")
    private Mitarbeiter referent;

    @ManyToOne
    @JoinColumn(name = "stellvertreter_id")
    private Mitarbeiter stellvertreter;

    @ManyToOne
    @JoinColumn(name = "fachbereich_id")
    private Fachbereich fachbereich;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
