package com.projekt.studiengangsorganisation.entity;

public enum ModulArt {

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

    ModulArt(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

}
