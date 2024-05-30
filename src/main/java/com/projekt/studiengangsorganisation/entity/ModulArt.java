package com.projekt.studiengangsorganisation.entity;

/**
 * Enumerationsklasse, die verschiedene Arten von Modulen repräsentiert.
 */
public enum ModulArt {

    /**
     * Die Arten eines Moduls.
     */
    VORLESUNG("Vorlesung"),
    SEMINAR("Seminar"),
    UEBUNG("Übung"),
    PRAKTIKUM("Praktikum"),
    TUTORIUM("Tutorium"),
    BLOCKVERANSTALTUNG("Blockveranstaltung"),
    PROJEKTARBEIT("Projektarbeit"),
    WORKSHOP("Workshop"),
    ONLINEKURS("Online-Kurs"),
    HYBRIDKURS("Hybridkurs"),
    SELBSTSTUDIUM("Selbststudium"),
    KOLLOQUIUM("Kolloquium"),
    PRAESENTATION("Präsentation"),
    SIMULATION("Simulation/Rollenspiel");

    private final String bezeichnung;

    /**
     * Konstruktor für ModulArt.
     * @param bezeichnung Die Bezeichnung der Modulart.
     */
    ModulArt(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    /**
     * Gibt die Bezeichnung der Modulart zurück.
     * @return Die Bezeichnung der Modulart.
     */
    public String getBezeichnung() {
        return bezeichnung;
    }
}