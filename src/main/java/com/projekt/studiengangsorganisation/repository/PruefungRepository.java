package com.projekt.studiengangsorganisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekt.studiengangsorganisation.entity.PruefungKey;

@Repository
public interface PruefungRepository extends JpaRepository<com.projekt.studiengangsorganisation.entity.Pruefung, Long> {

}
