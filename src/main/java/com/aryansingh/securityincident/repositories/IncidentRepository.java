package com.aryansingh.securityincident.repositories;

import com.aryansingh.securityincident.models.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident,Long> {

    boolean existsByTitle(String title);

    Optional<Incident> findByIncidentToken(String token);
}

