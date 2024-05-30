package com.projekt.studiengangsorganisation.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "Modul")
public class Modul {

    @Id
    @Column(name = "modul_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "modulnummer")
    private String modulnummer;

    @Column(name = "workload")
    private int workload;

    @Column(name = "credits")
    private int credits;

    @Column(name = "dauer")
    private int dauer;

    @Column(name = "Art")
    private String art;

    @Column(name = "abschluss")
    private String abschluss;

    @Column(name = "beschreibung")
    private String beschreibung;

    @Column(name = "lehrveranstaltungsort")
    private String lehrveranstaltungsort;

    @Column(name = "sprache")
    private String sprache;

    @Column(name = "freigegeben")
    private boolean freigegeben;

    @ManyToOne
    // @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "fachgruppe_id")
    private Fachgruppe fachgruppe;

    @Transient
    private Long fachgruppeId;

    @ManyToOne
    // @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "modulbeauftragter_id")
    private Mitarbeiter modulbeauftragter;

    @Transient
    private Long modulbeauftragterId;

    @OneToMany(mappedBy = "modul")
    @JsonIgnore
    Set<Pruefung> pruefungen;

    @ManyToOne
    // @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "modulgruppe_id")
    private Modulgruppe modulgruppe;

    @Transient
    private Long modulgruppeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModulnummer() {
        return modulnummer;
    }

    public void setModulnummer(String modulnummer) {
        this.modulnummer = modulnummer;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getDauer() {
        return dauer;
    }

    public void setDauer(int dauer) {
        this.dauer = dauer;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(String abschluss) {
        this.abschluss = abschluss;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getLehrveranstaltungsort() {
        return lehrveranstaltungsort;
    }

    public void setLehrveranstaltungsort(String lehrveranstaltungsort) {
        this.lehrveranstaltungsort = lehrveranstaltungsort;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public boolean isFreigegeben() {
        return freigegeben;
    }

    public void setFreigegeben(boolean freigegeben) {
        this.freigegeben = freigegeben;
    }

    public Fachgruppe getFachgruppe() {
        return fachgruppe;
    }

    public void setFachgruppe(Fachgruppe fachgruppe) {
        this.fachgruppe = fachgruppe;
    }

    public Mitarbeiter getModulbeauftragter() {
        return modulbeauftragter;
    }

    public void setModulbeauftragter(Mitarbeiter modulbeauftragter) {
        this.modulbeauftragter = modulbeauftragter;
    }

    public Set<Pruefung> getPruefungen() {
        return pruefungen;
    }

    public void setPruefungen(Set<Pruefung> pruefungen) {
        this.pruefungen = pruefungen;
    }

    public Modulgruppe getModulgruppe() {
        return modulgruppe;
    }

    public void setModulgruppe(Modulgruppe modulgruppe) {
        this.modulgruppe = modulgruppe;
    }

    public void setFachgruppeId(Long fachgruppeId) {
        this.fachgruppeId = fachgruppeId;
    }

    public Long getFachgruppeId() {
        return fachgruppeId;
    }

    public Long getModulgruppeId() {
        return modulgruppeId;
    }

    public void setModulgruppeId(Long modulgruppeId) {
        this.modulgruppeId = modulgruppeId;
    }

    public Long getModulbeauftragterId() {
        return modulbeauftragterId;
    }

    public void setModulbeauftragterId(Long modulbeauftragterId) {
        this.modulbeauftragterId = modulbeauftragterId;
    }

}