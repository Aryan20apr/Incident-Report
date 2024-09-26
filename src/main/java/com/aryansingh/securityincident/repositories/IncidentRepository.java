package com.aryansingh.securityincident.repositories;

import com.aryansingh.securityincident.models.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident,Long> {

}
