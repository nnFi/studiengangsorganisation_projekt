package com.projekt.studiengangsorganisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository
        extends JpaRepository<com.projekt.studiengangsorganisation.entity.Admin, Long> {

    Optional<com.projekt.studiengangsorganisation.entity.Admin> findByUsername(String username);

}
