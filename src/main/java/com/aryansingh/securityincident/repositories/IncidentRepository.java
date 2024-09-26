package com.aryansingh.securityincident.repositories;

import com.aryansingh.securityincident.models.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident,Long> {

    boolean existsByTitle(String title);


    Optional<Incident> findByIncidentToken(String token);


    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Incident i WHERE i.title = :title AND i.incidentToken <> :token")
    boolean existsByTitleAndNotIncidentToken(@Param("title") String title, @Param("token") String incidentToken);
}

