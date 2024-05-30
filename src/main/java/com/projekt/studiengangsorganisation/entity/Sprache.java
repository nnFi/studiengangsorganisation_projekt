package com.projekt.studiengangsorganisation.entity;

public enum Sprache {

    DEUTSCH("Deutsch"),
    ENGLISCH("Englisch");

    private final String bezeichnung;

    Sprache(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

}
