package com.aryansingh.securityincident.repositories;

import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.models.enums.SeverityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident,Long>, JpaSpecificationExecutor<Incident> {

    boolean existsByTitle(String title);


    Optional<Incident> findByIncidentToken(String token);


    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Incident i WHERE i.title = :title AND i.incidentToken <> :token")
    boolean existsByTitleAndNotIncidentToken(@Param("title") String title, @Param("token") String incidentToken);


    @Query("SELECT i FROM Incident i " +
            "WHERE (:severityLevel IS NULL OR i.severityLevel = :severityLevel)"
    +
            "AND (:startDate IS NULL OR i.incidentDate >= :startDate) "
    +"AND (:endDate IS NULL OR i.incidentDate <= :endDate)")
    List<Incident> findAllWithFilters(@Param("severityLevel") SeverityLevel severityLevel,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}

