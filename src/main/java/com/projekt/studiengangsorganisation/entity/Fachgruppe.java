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
 * Repräsentiert eine Fachgruppe.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "fachgruppe")
public class Fachgruppe {

    /**
     * Die eindeutige ID der Fachgruppe.
     */
    @Id
    @Column(name = "fachgruppe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Der Name der Fachgruppe.
     */
    @Column(name = "name")
    private String name;

    /**
     * Das Kürzel der Fachgruppe.
     */
    @Column(name = "kuerzel")
    private String kuerzel;

    /**
     * Der Referent der Fachgruppe.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //wird in der JSON angezeigt bei Objekt Konvertierung
    @JoinColumn(name = "referent")
    private Mitarbeiter referent;

    /**
     * Die Referent-ID.
     */
    @Transient
    private Long referentId;

    /**
     * Der Stellvertreter der Fachgruppe.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //wird in der JSON angezeigt bei Objekt Konvertierung
    @JoinColumn(name = "stellvertreter_id")
    private Mitarbeiter stellvertreter;

    /**
     * Die Stellvertreter-ID.
     */
    @Transient
    private Long stellvertreterId;

    /**
     * Der Fachbereich, zu dem die Fachgruppe gehört.
     */
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //wird in der JSON angezeigt bei Objekt Konvertierung
    @JoinColumn(name = "fachbereich_id")
    private Fachbereich fachbereich;

    /**
     * Die Fachbereich-ID.
     */
    @Transient
    private Long fachbereichId;

    /**
     * Gibt die ID der Fachgruppe zurück.
     * @return die ID der Fachgruppe.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID der Fachgruppe.
     * @param id die ID der Fachgruppe.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Fachgruppe zurück.
     * @return der Name der Fachgruppe.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Fachgruppe.
     * @param name der Name der Fachgruppe.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Referenten der Fachgruppe zurück.
     * @return der Referent der Fachgruppe.
     */
    public Mitarbeiter getReferent() {
        return referent;
    }

    /**
     * Setzt den Referenten der Fachgruppe.
     * @param referent der Referent der Fachgruppe.
     */
    public void setReferent(Mitarbeiter referent) {
        this.referent = referent;
    }

    /**
     * Gibt den Stellvertreter der Fachgruppe zurück.
     * @return der Stellvertreter der Fachgruppe.
     */
    public Mitarbeiter getStellvertreter() {
        return stellvertreter;
    }

    /**
     * Setzt den Stellvertreter der Fachgruppe.
     * @param stellvertreter der Stellvertreter der Fachgruppe.
     */
    public void setStellvertreter(Mitarbeiter stellvertreter) {
        this.stellvertreter = stellvertreter;
    }

    /**
     * Gibt den Fachbereich der Fachgruppe zurück.
     * @return der Fachbereich der Fachgruppe.
     */
    public Fachbereich getFachbereich() {
        return fachbereich;
    }

    /**
     * Setzt den Fachbereich der Fachgruppe.
     * @param fachbereich der Fachbereich der Fachgruppe.
     */
    public void setFachbereich(Fachbereich fachbereich) {
        this.fachbereich = fachbereich;
    }

    /**
     * Gibt die Referent-ID zurück.
     * @return die Referent-ID.
     */
    public Long getReferentId() {
        return referentId;
    }

    /**
     * Setzt die Referent-ID.
     * @param referentId die Referent-ID.
     */
    public void setReferentId(Long referentId) {
        this.referentId = referentId;
    }

    /**
     * Gibt die Stellvertreter-ID zurück.
     * @return die Stellvertreter-ID.
     */
    public Long getStellvertreterId() {
        return stellvertreterId;
    }

    /**
     * Setzt die Stellvertreter-ID.
     * @param stellvertreterId die Stellvertreter-ID.
     */
    public void setStellvertreterId(Long stellvertreterId) {
        this.stellvertreterId = stellvertreterId;
    }

    /**
     * Gibt die Fachbereich-ID zurück.
     * @return die Fachbereich-ID.
     */
    public Long getFachbereichId() {
        return fachbereichId;
    }

    /**
     * Setzt die Fachbereich-ID.
     * @param fachbereichId die Fachbereich-ID.
     */
    public void setFachbereichId(Long fachbereichId) {
        this.fachbereichId = fachbereichId;
    }

    /**
     * Gibt das Kuerzel zurück.
     * @return das Kuerzel.
     */
    public String getKuerzel() {
        return kuerzel;
    }

    /**
     * Setzt das Kürzel der Fachgruppe.
     * @param kuerzel das Kürzel der Fachgruppe.
     */
    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }
}
