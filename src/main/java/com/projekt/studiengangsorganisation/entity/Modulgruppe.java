package com.projekt.studiengangsorganisation.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "modulgruppe")
public class Modulgruppe {

    @Id
    @Column(name = "modulgruppe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "modulgruppe_modul", joinColumns = @jakarta.persistence.JoinColumn(name = "modulgruppe_id"), inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "modul_id"))
    private Set<Modul> module;

}