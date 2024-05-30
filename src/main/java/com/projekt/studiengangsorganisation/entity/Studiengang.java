package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Repräsentiert einen Studiengang.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "studiengang")
public class Studiengang {

    /** 
     * Die eindeutige ID des Studiengangs. 
     */
    @Id
    @Column(name = "studiengang_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 
     * Der Name des Studiengangs. 
     */
    @Column(name = "studiengang_name")
    private String name;

    /** 
     * Der Abschluss des Studiengangs (z.B. Bachelor, Master). 
     */
    @Column(name = "studiengang_abschluss")
    @Enumerated(EnumType.ORDINAL)
    private Abschluss abschluss;

    /** 
     * Die Regelstudienzeit des Studiengangs in Semestern. 
     */
    @Column(name = "studiengang_regelstudienzeit")
    private int regelstudienzeit;

    /** 
     * Der Leiter des Studiengangs. 
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "leiter_id")
    private Mitarbeiter leiter;

    /** 
     Die ID des Leiters des Studiengangs. 
     */
    @Transient
    private Long leiterId;

    /** 
     * Der stellvertretende Leiter des Studiengangs. 
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "stellvertretenderLeiter_id")
    private Mitarbeiter stellvertretenderLeiter;

    /** 
     * Die ID des stellvertretenden Leiters des Studiengangs. 
     */
    @Transient
    private Long stellvertreterId;

    /** 
     * Der Fachbereich, dem der Studiengang zugeordnet ist. 
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "fachbereich_id")
    private Fachbereich fachbereich;

    /** 
     * Die ID des Fachbereichs. 
     */
    @Transient
    private Long fachbereichId;

    /**
     * Gibt die ID des Studiengangs zurück.
     * @return die ID des Studiengangs.
     */
    public long getId() {
        return id;
    }

    /**
     * Setzt die ID des Studiengangs.
     * @param id die ID des Studiengangs.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Studiengangs zurück.
     * @return der Name des Studiengangs.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Studiengangs.
     * @param name der Name des Studiengangs.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Abschluss des Studiengangs zurück.
     * @return der Abschluss des Studiengangs.
     */
    public Abschluss getAbschluss() {
        return abschluss;
    }

    /**
     * Setzt den Abschluss des Studiengangs.
     * @param abschluss der Abschluss des Studiengangs.
     */
    public void setAbschluss(Abschluss abschluss) {
        this.abschluss = abschluss;
    }

    /**
     * Gibt die Regelstudienzeit des Studiengangs zurück.
     * @return die Regelstudienzeit des Studiengangs.
     */
    public int getRegelstudienzeit() {
        return regelstudienzeit;
    }

    /**
     * Setzt die Regelstudienzeit des Studiengangs.
     * @param regelstudienzeit die Regelstudienzeit des Studiengangs.
     */
    public void setRegelstudienzeit(int regelstudienzeit) {
        this.regelstudienzeit = regelstudienzeit;
    }

    /**
     * Gibt den Leiter des Studiengangs zurück.
     * @return der Leiter des Studiengangs.
     */
    public Mitarbeiter getLeiter() {
        return leiter;
    }

    /**
     * Setzt den Leiter des Studiengangs.
     * @param leiter der Leiter des Studiengangs.
     */
    public void setLeiter(Mitarbeiter leiter) {
        this.leiter = leiter;
    }

    /**
     * Gibt den stellvertretenden Leiter des Studiengangs zurück.
     * @return der stellvertretende Leiter des Studiengangs.
     */
    public Mitarbeiter getStellvertretenderLeiter() {
        return stellvertretenderLeiter;
    }

    /**
     * Setzt den stellvertretenden Leiter des Studiengangs.
     * @param stellvertretenderLeiter der stellvertretende Leiter des Studiengangs.
     */
    public void setStellvertretenderLeiter(Mitarbeiter stellvertretenderLeiter) {
        this.stellvertretenderLeiter = stellvertretenderLeiter;
    }

    /**
     * Gibt den Fachbereich des Studiengangs zurück.
     * @return der Fachbereich des Studiengangs.
     */
    public Fachbereich getFachbereich() {
        return fachbereich;
    }

    /**
     * Setzt den Fachbereich des Studiengangs.
     * @param fachbereich der Fachbereich des Studiengangs.
     */
    public void setFachbereich(Fachbereich fachbereich) {
        this.fachbereich = fachbereich;
    }

    /**
     * Gibt die ID des Leiters des Studiengangs zurück.
     * @return die ID des Leiters des Studiengangs.
     */
    public Long getLeiterId() {
        return leiterId;
    }

    /**
     * Setzt die ID des Leiters des Studiengangs.
     * @param leiterId die ID des Leiters des Studiengangs.
     */
    public void setLeiterId(Long leiterId) {
        this.leiterId = leiterId;
    }

    /**
     * Gibt die ID des stellvertretenden Leiters des Studiengangs zurück.
     * @return die ID des stellvertretenden Leiters des Studiengangs.
     */
    public Long getStellvertreterId() {
        return stellvertreterId;
    }

    /**
     * Setzt die ID des stellvertretenden Leiters des Studiengangs.
     * @param stellvertreterId die ID des stellvertretenden Leiters des Studiengangs.
     */
    public void setStellvertreterId(Long stellvertreterId) {
        this.stellvertreterId = stellvertreterId;
    }

    /**
     * Gibt die ID des Fachbereichs des Studiengangs zurück.
     * @return die ID des Fachbereichs des Studiengangs.
     */
    public Long getFachbereichId() {
        return fachbereichId;
    }

    /**
     * Setzt die ID des Fachbereichs des Studiengangs.
     * @param fachbereichId die ID des Fachbereichs des Studiengangs.
     */
    public void setFachbereichId(Long fachbereichId) {
        this.fachbereichId = fachbereichId;
    }
}