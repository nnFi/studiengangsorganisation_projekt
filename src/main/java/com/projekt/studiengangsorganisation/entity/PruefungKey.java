package com.projekt.studiengangsorganisation.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PruefungKey implements Serializable {

    @Column(name = "modul_id")
    Long modulId;

    @Column(name = "pruefungsordnung_id")
    Long pruefungsordnungId;

    public Long getModulId() {
        return modulId;
    }

    public void setModulId(Long modulId) {
        this.modulId = modulId;
    }

    public Long getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    public void setPruefungsordnungId(Long pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }

}
