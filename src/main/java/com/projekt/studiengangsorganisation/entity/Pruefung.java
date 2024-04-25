package com.projekt.studiengangsorganisation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "pruefung")
public class Pruefung {

    @EmbeddedId
    PruefungKey id;

    @ManyToOne
    @MapsId("moduliI")
    @JoinColumn(name = "modul_id")
    private Modul modul;

    @ManyToOne
    @MapsId("pruefungsordnungId")
    @JoinColumn(name = "pruefungsordnung_id")
    private Pruefungsordnung pruefungsordnung;

    @Column(name = "pruefungsnummer")
    private int pruefungsnummer;

    @Column(name = "fachsemester")
    private int fachsemester;

    public PruefungKey getId() {
        return id;
    }

    public void setId(PruefungKey id) {
        this.id = id;
    }

    public Modul getModul() {
        return modul;
    }

    public void setModul(Modul modul) {
        this.modul = modul;
    }

    public Pruefungsordnung getPruefungsordnung() {
        return pruefungsordnung;
    }

    public void setPruefungsordnung(Pruefungsordnung pruefungsordnung) {
        this.pruefungsordnung = pruefungsordnung;
    }

    public int getPruefungsnummer() {
        return pruefungsnummer;
    }

    public void setPruefungsnummer(int pruefungsnummer) {
        this.pruefungsnummer = pruefungsnummer;
    }

    public int getFachsemester() {
        return fachsemester;
    }

    public void setFachsemester(int fachsemester) {
        this.fachsemester = fachsemester;
    }

}
