package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
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
@Table(name = "pruefung")
public class Pruefung {

    /* @EmbeddedId */
    /* PruefungKey id; */
    @Id
    @Column(name = "pruefung_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    // @MapsId("modulId")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "modul_id")
    private Modul modul;

@Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String modulId;


    @ManyToOne
    // @MapsId("pruefungsordnungId")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "pruefungsordnung_id")
    private Pruefungsordnung pruefungsordnung;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pruefungsordnungId;

    @Column(name = "pruefungsnummer")
    private int pruefungsnummer;

    @Column(name = "fachsemester")
    private int fachsemester;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getModulId() {
        return modulId;
    }

    public void setModulId(String modulId) {
        this.modulId = modulId;
    }

    public String getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    public void setPruefungsordnungId(String pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }
    
}
