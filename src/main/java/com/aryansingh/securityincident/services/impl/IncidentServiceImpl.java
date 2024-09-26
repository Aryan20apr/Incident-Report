package com.aryansingh.securityincident.services.impl;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.repositories.IncidentRepository;
import com.aryansingh.securityincident.services.IncidentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    @Override
    public String createIncident(IncidentDTO incidentDTO) {

        validateIncident(incidentDTO);
        Incident incident = Incident.builder()
                .title(incidentDTO.getTitle())
                .description(incidentDTO.getDescription())
                .incidentDate(LocalDateTime.parse(incidentDTO.getIncidentDate()))
                .status(incidentDTO.getStatus())
                .notes(incidentDTO.getNotes())
                .severityLevel(SeverityLevel.valueOf(incidentDTO.getSeverityLevel().toUpperCase()))
                .status(incidentDTO.getStatus())
                .build();

        incidentRepository.save(incident);

        return incident.getIncidentToken();
    }

    @Override
    public void updateIncident(IncidentDTO incidentDTO) {
    }

    @Override
    public List<IncidentDTO> getAllIncidents() {
        return List.of();
    }

    @Override
    public IncidentDTO findIncidentById(String token) {
        return null;
    }

    private void validateIncident(IncidentDTO incident) {

        // Validate title length
        if (incident.getTitle().length() < 10) {
            throw new IllegalArgumentException("Title must be at least 10 characters long");
        }

        // Check for duplicate title
        if (incidentRepository.existsByTitle(incident.getTitle())) {
            throw new IllegalArgumentException("An incident with this title already exists");
        }


        // Check for invalid Severity Level
       boolean isMatched =  Arrays.stream(SeverityLevel.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(incident.getSeverityLevel()));

        if (!isMatched) {
            throw new IllegalArgumentException("Invalid severity level. Must be LOW, MEDIUM, or HIGH.");
        }

        // Check for invalid date
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime incidentDate =  LocalDateTime.parse(incident.getIncidentDate());
        if (incidentDate.isBefore(thirtyDaysAgo) || incidentDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Incident date must be within the last 30 days and not in the future");
        }
    }
}
