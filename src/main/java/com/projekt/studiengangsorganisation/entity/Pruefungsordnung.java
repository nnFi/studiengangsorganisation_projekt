package com.projekt.studiengangsorganisation.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
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

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @OneToMany(mappedBy = "pruefungsordnung")
    @JsonIdentityReference(alwaysAsId = true)
    // @JsonIgnore
    Set<Pruefung> pruefungen;

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

    public Set<Pruefung> getPruefungen() {
        return pruefungen;
    }

    public void setPruefungen(Set<Pruefung> pruefungen) {
        this.pruefungen = pruefungen;
    }

    public boolean isAuslaufend() {
        return auslaufend;
    }

    public void setAuslaufend(boolean auslaufend) {
        this.auslaufend = auslaufend;
    }
}
