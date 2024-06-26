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
 * Repräsentiert einen Fachbereich.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "fachbereich")
public class Fachbereich {

    /**
     * Die eindeutige ID des Fachbereichs.
     */
    @Id
    @Column(name = "fachbereich_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Der Name des Fachbereichs.
     */
    @Column(name = "name")
    private String name;

    /**
     * Der Referent des Fachbereichs.
     */
    @ManyToOne(optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "referent_id")
    private Mitarbeiter referent;

    /**
     *Die Referent-ID.
     */
    @Transient
    private Long referentId;

    /**
     * Der Stellvertreter des Fachbereichs.
     */
    @ManyToOne(optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //wird in der JSON angezeigt bei Objekt Konvertierung
    @JoinColumn(name = "stellvertreter_id")
    private Mitarbeiter stellvertreter;

    /**
     * Die Stellvertreter-ID.
     */
    @Transient
    private Long stellvertreterId;

    // Getter und Setter

    /**
     * Gibt die ID des Fachbereichs zurück.
     * @return die ID des Fachbereichs.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Fachbereichs.
     * @param id die ID des Fachbereichs.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Fachbereichs zurück.
     * @return der Name des Fachbereichs.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Fachbereichs.
     * @param name der Name des Fachbereichs.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Referenten des Fachbereichs zurück.
     * @return der Referent des Fachbereichs.
     */
    public Mitarbeiter getReferent() {
        return referent;
    }

    /**
     * Setzt den Referenten des Fachbereichs.
     * @param referent der Referent des Fachbereichs.
     */
    public void setReferent(Mitarbeiter referent) {
        this.referent = referent;
    }

    /**
     * Gibt den Stellvertreter des Fachbereichs zurück.
     * @return der Stellvertreter des Fachbereichs.
     */
    public Mitarbeiter getStellvertreter() {
        return stellvertreter;
    }

    /**
     * Setzt den Stellvertreter des Fachbereichs.
     * @param stellvertreter der Stellvertreter des Fachbereichs.
     */
    public void setStellvertreter(Mitarbeiter stellvertreter) {
        this.stellvertreter = stellvertreter;
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
}