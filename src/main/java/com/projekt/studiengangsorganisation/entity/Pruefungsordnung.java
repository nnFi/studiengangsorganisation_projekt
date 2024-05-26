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

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pruefungsordnung")
public class Pruefungsordnung {

    @Id
    @Column(name = "pruefungsordnung_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version;

    @Column(name = "freigegeben")
    private boolean freigegeben;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "studiengang_id")
    private Studiengang studiengang;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String studiengangId;

    @OneToMany(mappedBy = "pruefungsordnung")
    @JsonIdentityReference(alwaysAsId = true)
    // @JsonIgnore
    List<Pruefung> pruefungen;

    @Column(name = "auslafend")
    private boolean auslaufend;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isFreigegeben() {
        return freigegeben;
    }

    public void setFreigegeben(boolean freigegeben) {
        this.freigegeben = freigegeben;
    }

    public Studiengang getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(Studiengang studiengang) {
        this.studiengang = studiengang;
    }

    public List<Pruefung> getPruefungen() {
        return pruefungen;
    }

    public void setPruefungen(List<Pruefung> pruefungen) {
        this.pruefungen = pruefungen;
    }

    public boolean isAuslaufend() {
        return auslaufend;
    }

    public void setAuslaufend(boolean auslaufend) {
        this.auslaufend = auslaufend;
    }

    public String getStudiengangId() {
        return studiengangId;
    }

    public void setStudiengangId(String studiengangId) {
        this.studiengangId = studiengangId;
    }

}
