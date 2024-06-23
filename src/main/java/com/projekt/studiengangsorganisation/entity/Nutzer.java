package com.projekt.studiengangsorganisation.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * Abstrakte Klasse, die einen Nutzer im System repräsentiert.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class Nutzer {

    /**
     * Die eindeutige ID des Nutzers.
     */
    @Id
    @Column(name = "nutzer_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Der Nachname des Nutzers.
     */
    @Column(name = "nutzer_nachname")
    private String nachname;

    /**
     * Der Vorname des Nutzers.
     */
    @Column(name = "nutzer_vorname")
    private String vorname;

    /**
     * Der Benutzername des Nutzers.
     */
    @Column(name = "username", unique = true)
    private String username;

    /**
     * Das Passwort des Nutzers.
     */
    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Die Rolle des Nutzers.
     */
    @Column(name = "role")
    private String role;

    /**
     * Gibt die ID des Nutzers zurück.
     * 
     * @return die ID des Nutzers.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Nutzers.
     * 
     * @param id die ID des Nutzers.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Nachnamen des Nutzers zurück.
     * 
     * @return der Nachname des Nutzers.
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * Setzt den Nachnamen des Nutzers.
     * 
     * @param nachname der Nachname des Nutzers.
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Gibt den Vornamen des Nutzers zurück.
     * 
     * @return der Vorname des Nutzers.
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Setzt den Vornamen des Nutzers.
     * 
     * @param vorname der Vorname des Nutzers.
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Gibt den Benutzernamen des Nutzers zurück.
     * 
     * @return der Benutzername des Nutzers.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setzt den Benutzernamen des Nutzers.
     * 
     * @param username der Benutzername des Nutzers.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gibt das Passwort des Nutzers zurück.
     * 
     * @return das Passwort des Nutzers.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort des Nutzers.
     * 
     * @param password das Passwort des Nutzers.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt die Rolle des Nutzers zurück.
     * 
     * @return die Rolle des Nutzers.
     */
    public String getRole() {
        return role;
    }

    /**
     * Setzt die Rolle des Nutzers.
     * 
     * @param role die Rolle des Nutzers.
     */
    public void setRole(String role) {
        this.role = role;
    }
}