package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutzerRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Nutzer, Long> {

    Optional<com.projekt.studiengangsorganisation.entity.Nutzer> findByUsername(String username);

}
