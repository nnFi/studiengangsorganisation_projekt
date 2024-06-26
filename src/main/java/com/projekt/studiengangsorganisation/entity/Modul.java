package com.projekt.studiengangsorganisation.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Entität, die ein Modul repräsentiert.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "Modul")
public class Modul {

    /**
     * Die eindeutige ID des Moduls.
     */
    @Id
    @Column(name = "modul_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name des Moduls.
     */
    @Column(name = "name")
    private String name;

    /**
     * Nummer des Moduls.
     */
    @Column(name = "modulnummer")
    private String modulnummer;

    /**
     * Arbeitsbelastung des Moduls.
     */
    @Column(name = "workload")
    private int workload;

    /**
     * Anzahl der Credits des Moduls.
     */
    @Column(name = "credits")
    private int credits;

    /**
     * Dauer des Moduls.
     */
    @Column(name = "dauer")
    private int dauer;

    /**
     * Art des Moduls.
     */
    @Column(name = "Art")
    @Enumerated(EnumType.ORDINAL)
    private ModulArt art;

    /**
     * Abschluss, für den das Modul relevant ist.
     */
    @Column(name = "abschluss")
    @Enumerated(EnumType.ORDINAL)
    private Abschluss abschluss;

    /**
     * Beschreibung des Moduls.
     */
    @Column(name = "beschreibung", columnDefinition = "TEXT")
    private String beschreibung;

    /**
     * Ort der Lehrveranstaltung des Moduls.
     */
    @Column(name = "lehrveranstaltungsort")
    private String lehrveranstaltungsort;

    /**
     * Sprache des Moduls.
     */
    @Column(name = "sprache")
    @Enumerated(EnumType.ORDINAL)
    private Sprache sprache;

    /**
     * Angabe, ob das Modul freigegeben ist.
     */
    @Column(name = "freigegeben")
    private boolean freigegeben;

    /**
     * Fachgruppe, zu der das Modul gehört.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "fachgruppe_id")
    private Fachgruppe fachgruppe;

    /**
     * Die Fachgruppe-ID.
     */
    @Transient
    private Long fachgruppeId;

    /**
     * Mitarbeiter, der für das Modul zuständig ist.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "modulbeauftragter_id")
    private Mitarbeiter modulbeauftragter;

    /**
     * Die Modulbeauftragter-ID.
     */
    @Transient
    private Long modulbeauftragterId;

    /**
     * Prüfungen, die dem Modul zugeordnet sind.
     */
    @OneToMany(mappedBy = "modul")
    @JsonIgnore
    Set<Pruefung> pruefungen;

    /**
     * Prüfungen, die dem Modul zugeordnet sind.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "modulgruppe_id")
    private Modulgruppe modulgruppe;

    /**
     * Die Modulgruppe-ID.
     */
    @Transient
    private Long modulgruppeId;

    /**
     * Gibt die ID des Moduls zurück.
     * 
     * @return Die ID des Moduls.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Moduls.
     * 
     * @param id Die ID des Moduls.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Moduls zurück.
     * 
     * @return Der Name des Moduls.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Moduls.
     * 
     * @param name Der Name des Moduls.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Modulnummer zurück.
     * 
     * @return Die Modulnummer.
     */
    public String getModulnummer() {
        return modulnummer;
    }

    /**
     * Setzt die Modulnummer.
     * 
     * @param modulnummer Die Modulnummer.
     */
    public void setModulnummer(String modulnummer) {
        this.modulnummer = modulnummer;
    }

    /**
     * Gibt die Arbeitsbelastung des Moduls zurück.
     * 
     * @return Die Arbeitsbelastung des Moduls.
     */
    public int getWorkload() {
        return workload;
    }

    /**
     * Setzt die Arbeitsbelastung des Moduls.
     * 
     * @param workload Die Arbeitsbelastung des Moduls.
     */
    public void setWorkload(int workload) {
        this.workload = workload;
    }

    /**
     * Gibt die Anzahl der Credits des Moduls zurück.
     * 
     * @return Die Anzahl der Credits des Moduls.
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Setzt die Anzahl der Credits des Moduls.
     * 
     * @param credits Die Anzahl der Credits des Moduls.
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Gibt die Dauer des Moduls zurück.
     * 
     * @return Die Dauer des Moduls.
     */
    public int getDauer() {
        return dauer;
    }

    /**
     * Setzt die Dauer des Moduls.
     * 
     * @param dauer Die Dauer des Moduls.
     */
    public void setDauer(int dauer) {
        this.dauer = dauer;
    }

    /**
     * Gibt die Art des Moduls zurück.
     * 
     * @return Die Art des Moduls.
     */
    public ModulArt getArt() {
        return art;
    }

    /**
     * Setzt die Art des Moduls.
     * 
     * @param art Die Art des Moduls.
     */
    public void setArt(ModulArt art) {
        this.art = art;
    }

    /**
     * Gibt den Abschluss zurück, für den das Modul relevant ist.
     * 
     * @return Der Abschluss, für den das Modul relevant ist.
     */
    public Abschluss getAbschluss() {
        return abschluss;
    }

    /**
     * Setzt den Abschluss, für den das Modul relevant ist.
     * 
     * @param abschluss Der Abschluss, für den das Modul relevant ist.
     */
    public void setAbschluss(Abschluss abschluss) {
        this.abschluss = abschluss;
    }

    /**
     * Gibt die Beschreibung des Moduls zurück.
     * 
     * @return Die Beschreibung des Moduls.
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * Setzt die Beschreibung des Moduls.
     * 
     * @param beschreibung Die Beschreibung des Moduls.
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * Gibt den Lehrveranstaltungsort des Moduls zurück.
     * 
     * @return Der Lehrveranstaltungsort des Moduls.
     */
    public String getLehrveranstaltungsort() {
        return lehrveranstaltungsort;
    }

    /**
     * Setzt den Lehrveranstaltungsort des Moduls.
     * 
     * @param lehrveranstaltungsort Der Lehrveranstaltungsort des Moduls.
     */
    public void setLehrveranstaltungsort(String lehrveranstaltungsort) {
        this.lehrveranstaltungsort = lehrveranstaltungsort;
    }

    /**
     * Gibt die Sprache des Moduls zurück.
     * 
     * @return Die Sprache des Moduls.
     */
    public Sprache getSprache() {
        return sprache;
    }

    /**
     * Setzt die Sprache des Moduls.
     * 
     * @param sprache Die Sprache des Moduls.
     */
    public void setSprache(Sprache sprache) {
        this.sprache = sprache;
    }

    /**
     * Gibt zurück, ob das Modul freigegeben ist.
     * 
     * @return True, wenn das Modul freigegeben ist, sonst false.
     */
    public boolean isFreigegeben() {
        return freigegeben;
    }

    /**
     * Setzt, ob das Modul freigegeben ist.
     * 
     * @param freigegeben True, wenn das Modul freigegeben ist, sonst false.
     */
    public void setFreigegeben(boolean freigegeben) {
        this.freigegeben = freigegeben;
    }

    /**
     * Gibt die Fachgruppe zurück, zu der das Modul gehört.
     * 
     * @return Die Fachgruppe des Moduls.
     */
    public Fachgruppe getFachgruppe() {
        return fachgruppe;
    }

    /**
     * Setzt die Fachgruppe, zu der das Modul gehört.
     * 
     * @param fachgruppe Die Fachgruppe des Moduls.
     */
    public void setFachgruppe(Fachgruppe fachgruppe) {
        this.fachgruppe = fachgruppe;
    }

    /**
     * Gibt den Mitarbeiter zurück, der für das Modul zuständig ist.
     * 
     * @return Der Mitarbeiter, der für das Modul zuständig ist.
     */
    public Mitarbeiter getModulbeauftragter() {
        return modulbeauftragter;
    }

    /**
     * Setzt den Mitarbeiter, der für das Modul zuständig ist.
     * 
     * @param modulbeauftragter Der Mitarbeiter, der für das Modul zuständig ist.
     */
    public void setModulbeauftragter(Mitarbeiter modulbeauftragter) {
        this.modulbeauftragter = modulbeauftragter;
    }

    /**
     * Gibt die Prüfungen zurück, die dem Modul zugeordnet sind.
     *
     * @return Die Prüfungen des Moduls.
     */
    public Set<Pruefung> getPruefungen() {
        return pruefungen;
    }

    /**
     * Setzt die Prüfungen, die dem Modul zugeordnet sind.
     *
     * @param pruefungen Die Prüfungen des Moduls.
     */
    public void setPruefungen(Set<Pruefung> pruefungen) {
        this.pruefungen = pruefungen;
    }

    /**
     * Gibt die Modulgruppe zurück, zu der das Modul gehört.
     *
     * @return Die Modulgruppe des Moduls.
     */
    public Modulgruppe getModulgruppe() {
        return modulgruppe;
    }

    /**
     * Setzt die Modulgruppe, zu der das Modul gehört.
     *
     * @param modulgruppe Die Modulgruppe des Moduls.
     */
    public void setModulgruppe(Modulgruppe modulgruppe) {
        this.modulgruppe = modulgruppe;
    }

    /**
     * Gibt die ID der Fachgruppe zurück.
     * 
     * @return Die ID der Fachgruppe.
     */
    public Long getFachgruppeId() {
        return fachgruppeId;
    }

    /**
     * Setzt die ID der Fachgruppe.
     * 
     * @param fachgruppeId Die ID der Fachgruppe.
     */
    public void setFachgruppeId(Long fachgruppeId) {
        this.fachgruppeId = fachgruppeId;
    }

    /**
     * Gibt die ID der Modulgruppe zurück.
     * 
     * @return Die ID der Modulgruppe.
     */
    public Long getModulgruppeId() {
        return modulgruppeId;
    }

    /**
     * Setzt die ID der Modulgruppe.
     * 
     * @param modulgruppeId Die ID der Modulgruppe.
     */
    public void setModulgruppeId(Long modulgruppeId) {
        this.modulgruppeId = modulgruppeId;
    }

    /**
     * Gibt die ID des Modulbeauftragten zurück.
     * 
     * @return Die ID des Modulbeauftragten.
     */
    public Long getModulbeauftragterId() {
        return modulbeauftragterId;
    }

    /**
     * Setzt die ID des Modulbeauftragten.
     * 
     * @param modulbeauftragterId Die ID des Modulbeauftragten.
     */
    public void setModulbeauftragterId(Long modulbeauftragterId) {
        this.modulbeauftragterId = modulbeauftragterId;
    }
}