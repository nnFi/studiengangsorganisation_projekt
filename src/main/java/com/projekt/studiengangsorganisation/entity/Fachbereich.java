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

    public Nutzer getReferent() {
        return referent;
    }

    public void setReferent(Mitarbeiter referent) {
        this.referent = referent;
    }

    public Nutzer getStellvertreter() {
        return stellvertreter;
    }

    public void setStellvertreter(Mitarbeiter stellvertreter) {
        this.stellvertreter = stellvertreter;
    }

    @ManyToOne()
    @JoinColumn(name = "referent_id")
    private Mitarbeiter referent;

    @ManyToOne()
    @JoinColumn(name = "stellvertreter_id")
    private Mitarbeiter stellvertreter;
}
