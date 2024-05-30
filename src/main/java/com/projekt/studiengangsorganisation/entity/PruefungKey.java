package com.projekt.studiengangsorganisation.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Repräsentiert den zusammengesetzten Schlüssel für eine Prüfung.
 */
@Embeddable
public class PruefungKey implements Serializable {

    /**
     * Die ID des Moduls.
     */
    @Column(name = "modul_id")
    Long modulId;

    /**
     * Die ID der Prüfungsordnung.
     */
    @Column(name = "pruefungsordnung_id")
    Long pruefungsordnungId;

    // Getter und Setter

    /**
     * Gibt die ID des Moduls zurück.
     * @return die ID des Moduls.
     */
    public Long getModulId() {
        return modulId;
    }

    /**
     * Setzt die ID des Moduls.
     * @param modulId die ID des Moduls.
     */
    public void setModulId(Long modulId) {
        this.modulId = modulId;
    }

    /**
     * Gibt die ID der Prüfungsordnung zurück.
     * @return die ID der Prüfungsordnung.
     */
    public Long getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    /**
     * Setzt die ID der Prüfungsordnung.
     * @param pruefungsordnungId die ID der Prüfungsordnung.
     */
    public void setPruefungsordnungId(Long pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }
}