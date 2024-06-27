package com.projekt.studiengangsorganisation.entity;

/**
 * Enumerationsklasse, die verschiedene Sprachen repräsentiert.
 * 
 * @author Paul Rakow
 */
public enum Sprache {

    DEUTSCH("Deutsch"),
    ENGLISCH("Englisch");

    private final String bezeichnung;

    /**
     * Konstruktor für Sprache.
     * 
     * @param bezeichnung Die Bezeichnung der Sprache.
     */
    Sprache(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    /**
     * Gibt die Bezeichnung der Sprache zurück.
     * 
     * @return Die Bezeichnung der Sprache.
     */
    public String getBezeichnung() {
        return bezeichnung;
    }
}