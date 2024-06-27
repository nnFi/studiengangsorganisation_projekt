package com.projekt.studiengangsorganisation.entity;

import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Repräsentiert eine Prüfungsordnung.
 * 
 * @author Finn Plassmeier
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pruefungsordnung")
public class Pruefungsordnung {

    /**
     * Die eindeutige ID der Prüfungsordnung.
     */
    @Id
    @Column(name = "pruefungsordnung_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Die Version der Prüfungsordnung.
     */
    @Column(name = "version")
    private String version;

    /**
     * Gibt an, ob die Prüfungsordnung freigegeben ist oder nicht.
     */
    @Column(name = "freigegeben")
    private boolean freigegeben;

    /**
     * Der zugehörige Studiengang der Prüfungsordnung.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // wird in der JSON angezeigt bei Objekt Konvertierung
    @JoinColumn(name = "studiengang_id")
    private Studiengang studiengang;

    /**
     * Die ID des zugehörigen Studiengangs.
     */
    @Transient
    private Long studiengangId;

    /**
     * Die Liste der Prüfungen, die dieser Prüfungsordnung zugeordnet sind.
     */
    @OneToMany(mappedBy = "pruefungsordnung")
    @JsonIdentityReference(alwaysAsId = true)
    List<Pruefung> pruefungen;

    /**
     * Gibt an, ob die Prüfungsordnung auslaufend ist oder nicht.
     */
    @Column(name = "auslaufend")
    private boolean auslaufend;

    /**
     * Gibt die ID der Prüfungsordnung zurück.
     * 
     * @return die ID der Prüfungsordnung.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID der Prüfungsordnung.
     * 
     * @param id die ID der Prüfungsordnung.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt die Version der Prüfungsordnung zurück.
     * 
     * @return die Version der Prüfungsordnung.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setzt die Version der Prüfungsordnung.
     * 
     * @param version die Version der Prüfungsordnung.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Überprüft, ob die Prüfungsordnung freigegeben ist.
     * 
     * @return true, wenn die Prüfungsordnung freigegeben ist, sonst false.
     */
    public boolean isFreigegeben() {
        return freigegeben;
    }

    /**
     * Setzt den Freigabestatus der Prüfungsordnung.
     * 
     * @param freigegeben true, wenn die Prüfungsordnung freigegeben ist, sonst
     *                    false.
     */
    public void setFreigegeben(boolean freigegeben) {
        this.freigegeben = freigegeben;
    }

    /**
     * Gibt den zugehörigen Studiengang der Prüfungsordnung zurück.
     * 
     * @return der zugehörige Studiengang der Prüfungsordnung.
     */
    public Studiengang getStudiengang() {
        return studiengang;
    }

    /**
     * Setzt den zugehörigen Studiengang der Prüfungsordnung.
     * 
     * @param studiengang der zugehörige Studiengang der Prüfungsordnung.
     */
    public void setStudiengang(Studiengang studiengang) {
        this.studiengang = studiengang;
    }

    /**
     * Gibt die Liste der Prüfungen zurück, die dieser Prüfungsordnung zugeordnet
     * sind.
     * 
     * @return die Liste der Prüfungen.
     */
    public List<Pruefung> getPruefungen() {
        return pruefungen;
    }

    /**
     * Setzt die Liste der Prüfungen, die dieser Prüfungsordnung zugeordnet sind.
     * 
     * @param pruefungen die Liste der Prüfungen.
     */
    public void setPruefungen(List<Pruefung> pruefungen) {
        this.pruefungen = pruefungen;
    }

    /**
     * Überprüft, ob die Prüfungsordnung auslaufend ist.
     * 
     * @return true, wenn die Prüfungsordnung auslaufend ist, sonst false.
     */
    public boolean isAuslaufend() {
        return auslaufend;
    }

    /**
     * Setzt den Auslaufstatus der Prüfungsordnung.
     * 
     * @param auslaufend true, wenn die Prüfungsordnung auslaufend ist, sonst false.
     */
    public void setAuslaufend(boolean auslaufend) {
        this.auslaufend = auslaufend;
    }

    /**
     * Gibt die ID des zugehörigen Studiengangs zurück.
     * 
     * @return die ID des zugehörigen Studiengangs.
     */
    public Long getStudiengangId() {
        return studiengangId;
    }

    /**
     * Setzt die ID des zugehörigen Studiengangs.
     * 
     * @param studiengangId die ID des zugehörigen Studiengangs.
     */
    public void setStudiengangId(Long studiengangId) {
        this.studiengangId = studiengangId;
    }
}
