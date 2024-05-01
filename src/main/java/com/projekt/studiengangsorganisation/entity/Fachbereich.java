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
@Table(name = "fachbereich")
public class Fachbereich {

    @Id
    @Column(name = "fachbereich_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "referent_id")
    private Mitarbeiter referent;

    @ManyToOne()
    @JoinColumn(name = "stellvertreter_id")
    private Mitarbeiter stellvertreter;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mitarbeiter getReferent() {
        return referent;
    }

    public void setReferent(Mitarbeiter referent) {
        this.referent = referent;
    }

    public Mitarbeiter getStellvertreter() {
        return stellvertreter;
    }

    public void setStellvertreter(Mitarbeiter stellvertreter) {
        this.stellvertreter = stellvertreter;
    }

}
