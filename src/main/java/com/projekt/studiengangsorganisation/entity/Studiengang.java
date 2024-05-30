package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

    // @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "leiter_id")
    private Mitarbeiter leiter;

    @Transient
    private Long leiterId;

    @ManyToOne
    // @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "stellvertretenderLeiter_id")
    private Mitarbeiter stellvertretenderLeiter;

    @Transient
    private Long stellvertreterId;

    @ManyToOne
    // @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "fachbereich_id")
    private Fachbereich fachbereich;

    @Transient
    private Long fachbereichId;

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

    public Long getLeiterId() {
        return leiterId;
    }

    public void setLeiterId(Long leiterId) {
        this.leiterId = leiterId;
    }

    public Long getStellvertreterId() {
        return stellvertreterId;
    }

    public void setStellvertreterId(Long stellvertreterId) {
        this.stellvertreterId = stellvertreterId;
    }

    public Long getFachbereichId() {
        return fachbereichId;
    }

    public void setFachbereichId(Long fachbereichId) {
        this.fachbereichId = fachbereichId;
    }
}