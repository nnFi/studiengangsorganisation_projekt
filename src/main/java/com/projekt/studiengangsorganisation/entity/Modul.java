package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Modul")
public class Modul {

    @Id
    @Column(name = "modul_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String testname; // hahahaha

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    @Column(name = "kuerzel")
    private String kuerzel;

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    @Column(name = "workload")
    private int workload;

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    @Column(name = "credits")
    private int credits;

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Column(name = "dauer")
    private int dauer;

    public int getDauer() {
        return dauer;
    }

    public void setDauer(int dauer) {
        this.dauer = dauer;
    }

    @Column(name = "Art")
    private String art;

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    @Column(name = "abschluss")
    private String abschluss;

    public String getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(String abschluss) {
        this.abschluss = abschluss;
    }

    @Column(name = "beschreibung")
    private String beschreibung;

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Column(name = "lehrveranstaltungsort")
    private String lehrveranstaltungsort;

    public String getLehrveranstaltungsort() {
        return lehrveranstaltungsort;
    }

    public void setLehrveranstaltungsort(String lehrveranstaltungsort) {
        this.lehrveranstaltungsort = lehrveranstaltungsort;
    }

    @Column(name = "sprache")
    private String sprache;

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    @Column(name = "freigegeben")
    private boolean freigegeben;

    public boolean getFreigegeben() {
        return freigegeben;
    }

    public void setFreigegeben(boolean freigegeben) {
        this.freigegeben = freigegeben;
    }

    @Column(name = "fachgruppe")
    private Fachgruppe fachgruppe;

    @Column(name = "modulbeauftragter")
    private Nutzer modulbeauftrager;
}
