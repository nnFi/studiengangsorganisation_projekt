package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "studiengang")
public class Studiengang {

    @Id
    @Column(name = "studiengang_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "studiengang_name")
    private String name;

    @Column(name = "studiengang_abschluss")
    private String abschluss;

    @Column(name = "studiengang_regelstudienzeit")
    private int regelstudienzeit;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "leiter_id")
    private Mitarbeiter leiter;

    private String leiterId;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "stellvertretenderLeiter_id")
    private Mitarbeiter stellvertretenderLeiter;

    private String stellvertreterId;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "fachbereich_id")
    private Fachbereich fachbereich;

    private String fachbereichId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(String abschluss) {
        this.abschluss = abschluss;
    }

    public int getRegelstudienzeit() {
        return regelstudienzeit;
    }

    public void setRegelstudienzeit(int regelstudienzeit) {
        this.regelstudienzeit = regelstudienzeit;
    }

    public Mitarbeiter getLeiter() {
        return leiter;
    }

    public void setLeiter(Mitarbeiter leiter) {
        this.leiter = leiter;
    }

    public Mitarbeiter getStellvertretenderLeiter() {
        return stellvertretenderLeiter;
    }

    public void setStellvertretenderLeiter(Mitarbeiter stellvertretenderLeiter) {
        this.stellvertretenderLeiter = stellvertretenderLeiter;
    }

    public Fachbereich getFachbereich() {
        return fachbereich;
    }

    public void setFachbereich(Fachbereich fachbereich) {
        this.fachbereich = fachbereich;
    }

    public String getLeiterId() {
        return leiterId;
    }

    public void setLeiterId(String leiterId) {
        this.leiterId = leiterId;
    }

    public String getStellvertreterId() {
        return stellvertreterId;
    }

    public void setStellvertreterId(String stellvertreterId) {
        this.stellvertreterId = stellvertreterId;
    }

    public String getFachbereichId() {
        return fachbereichId;
    }

    public void setFachbereichId(String fachbereichId) {
        this.fachbereichId = fachbereichId;
    }
}