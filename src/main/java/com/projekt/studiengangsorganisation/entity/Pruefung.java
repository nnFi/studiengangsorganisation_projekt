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

/**
 * Repräsentiert eine Prüfung.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pruefung")
public class Pruefung {

    /**
     * Die eindeutige ID der Prüfung.
     */
    @Id
    @Column(name = "pruefung_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Das Modul, zu dem die Prüfung gehört.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "modul_id")
    private Modul modul;

    /**
     * Die Modul-ID.
     */
    @Transient
    private Long modulId;

    /**
     * Die Prüfungsordnung, zu der die Prüfung gehört.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "pruefungsordnung_id")
    private Pruefungsordnung pruefungsordnung;

    /**
     * Die Prüfungsordnung-ID.
     */
    @Transient
    private Long pruefungsordnungId;

    /**
     * Die Prüfungsnummer.
     */
    @Column(name = "pruefungsnummer")
    private int pruefungsnummer;

    /**
     * Das Fachsemester, in dem die Prüfung stattfindet.
     */
    @Column(name = "fachsemester")
    private int fachsemester;

    /**
     * Gibt die ID der Prüfung zurück.
     * @return die ID der Prüfung.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID der Prüfung.
     * @param id die ID der Prüfung.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt das Modul zurück, zu dem die Prüfung gehört.
     * @return das Modul der Prüfung.
     */
    public Modul getModul() {
        return modul;
    }

    /**
     * Setzt das Modul der Prüfung.
     * @param modul das Modul der Prüfung.
     */
    public void setModul(Modul modul) {
        this.modul = modul;
    }

    /**
     * Gibt die Prüfungsordnung zurück, zu der die Prüfung gehört.
     * @return die Prüfungsordnung der Prüfung.
     */
    public Pruefungsordnung getPruefungsordnung() {
        return pruefungsordnung;
    }

    /**
     * Setzt die Prüfungsordnung der Prüfung.
     * @param pruefungsordnung die Prüfungsordnung der Prüfung.
     */
    public void setPruefungsordnung(Pruefungsordnung pruefungsordnung) {
        this.pruefungsordnung = pruefungsordnung;
    }

    /**
     * Gibt die Prüfungsnummer zurück.
     * @return die Prüfungsnummer.
     */
    public int getPruefungsnummer() {
        return pruefungsnummer;
    }

    /**
     * Setzt die Prüfungsnummer.
     * @param pruefungsnummer die Prüfungsnummer.
     */
    public void setPruefungsnummer(int pruefungsnummer) {
        this.pruefungsnummer = pruefungsnummer;
    }

    /**
     * Gibt das Fachsemester zurück.
     * @return das Fachsemester.
     */
    public int getFachsemester() {
        return fachsemester;
    }

    /**
     * Setzt das Fachsemester.
     * @param fachsemester das Fachsemester.
     */
    public void setFachsemester(int fachsemester) {
        this.fachsemester = fachsemester;
    }

    /**
     * Gibt die ID des Moduls zurück.
     * @return die ID des Moduls.
     */
    public Long getModulId() {
        return modulId;
    }

    /**
     * Setzt die ID des Moduls.
     * @param modulId die ID des Moduls.
     */
    public void setModulId(Long modulId) {
        this.modulId = modulId;
    }

    /**
     * Gibt die ID der Prüfungsordnung zurück.
     * @return die ID der Prüfungsordnung.
     */
    public Long getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    /**
     * Setzt die ID der Prüfungsordnung.
     * @param pruefungsordnungId die ID der Prüfungsordnung.
     */
    public void setPruefungsordnungId(Long pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }
}