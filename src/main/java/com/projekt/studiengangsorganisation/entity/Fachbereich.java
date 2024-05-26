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
@Table(name = "fachbereich")
public class Fachbereich {

    @Id
    @Column(name = "fachbereich_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "referent_id")
    private Mitarbeiter referent;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String referentId;

    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "stellvertreter_id")
    private Mitarbeiter stellvertreter;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String stellvertreterId;

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

    public Mitarbeiter getReferent() {
        return referent;
    }

    public void setReferent(Mitarbeiter referent) {
        this.referent = referent;
    }

    public Mitarbeiter getStellvertreter() {
        return stellvertreter;
    }

    public void setStellvertreter(Mitarbeiter stellvertreter) {
        this.stellvertreter = stellvertreter;
    }

    public String getReferentId() {
        return referentId;
    }

    public void setReferentId(String referentId) {
        this.referentId = referentId;
    }

    public String getStellvertreterId() {
        return stellvertreterId;
    }

    public void setStellvertreterId(String stellvertreterId) {
        this.stellvertreterId = stellvertreterId;
    }

}
