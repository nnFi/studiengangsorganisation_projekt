package com.projekt.studiengangsorganisation.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fachbereich")
public class Fachbereich{

    @Id
    @Column(name = "fachbereich_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Nutzer getReferent() {
        return referent;
    }

    public void setReferent(Nutzer referent) {
        this.referent = referent;
    }

    public Nutzer getStellvertreter() {
        return stellvertreter;
    }

    public void setStellvertreter(Nutzer stellvertreter) {
        this.stellvertreter = stellvertreter;
    }

    public Set<Fachgruppe> getFachgruppen() {
        return fachgruppen;
    }

    public void setFachgruppen(Set<Fachgruppe> fachgruppen) {
        this.fachgruppen = fachgruppen;
    }

    @ManyToOne()
    @Column(name = "fachbereich_referent")
    @JoinColumn(name = "referent_id")
    private Nutzer referent;

    @ManyToMany()
    @Column(name = "fachbereich_stellvertreter")
    @JoinColumn(name = "stellvertreter_id")
    private Nutzer stellvertreter;

    @ManyToMany
    @JoinColumn(name = "fachgruppe_id")
    Set<Fachgruppe> fachgruppen;
}
