package com.projekt.studiengangsorganisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutzerRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Nutzer, Long> {

}
