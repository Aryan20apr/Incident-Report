package com.aryansingh.securityincident.services.impl;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.models.enums.Status;
import com.aryansingh.securityincident.repositories.IncidentRepository;
import com.aryansingh.securityincident.services.IncidentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    @Override
    public String createIncident(IncidentDTO incidentDTO) {

        validateIncident(incidentDTO);
        validateNewIncidentStatus(incidentDTO);
        Incident incident = Incident.builder()
                .title(incidentDTO.getTitle())
                .incidentDate(LocalDateTime.parse(incidentDTO.getIncidentDate()))
                .status(Status.OPEN)
                .notes(incidentDTO.getNotes())
                .severityLevel(SeverityLevel.valueOf(incidentDTO.getSeverityLevel().toUpperCase()))
                .build();

        incidentRepository.save(incident);

        return incident.getIncidentToken();
    }

    @Override
    public IncidentDTO updateIncident(IncidentDTO incidentDTO) {

        validateExistingIncident(incidentDTO);

        if(incidentDTO.getIncidentToken()==null){
            throw new IllegalArgumentException("Incident token cannot be empty");
        } else{
            String title = incidentDTO.getTitle();
            String token = incidentDTO.getIncidentToken();

            if(incidentRepository.existsByTitleAndNotIncidentToken(title,token))
                throw new IllegalArgumentException("Incident with given title already exists");

            Optional<Incident> optionalIncident = incidentRepository.findByIncidentToken(token);

            if(optionalIncident.isEmpty()){
                throw new IllegalArgumentException("Incident with given token does not exist");
            } else{

                Incident incident = optionalIncident.get();
                incident.setTitle(title);
                incident.setIncidentDate(LocalDateTime.parse(incidentDTO.getIncidentDate()));

                incident.setStatus(Status.valueOf(incidentDTO.getStatus().toUpperCase()));
                incident.setNotes(incidentDTO.getNotes());
                incident.setSeverityLevel(SeverityLevel.valueOf(incidentDTO.getSeverityLevel().toUpperCase()));

                incidentRepository.save(incident);
                incidentDTO.setUpdatedAt(incident.getUpdatedAt().toString());
                return incidentDTO;

            }
        }
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


        // Check for duplicate title
        if (incidentRepository.existsByTitle(incident.getTitle())) {
            throw new IllegalArgumentException("An incident with this title already exists");
        }
        validateSeverity(incident);
        validateDate(incident);
    }

    private static void validateSeverity(IncidentDTO incident) {
        // Check for invalid Severity Level
        boolean isMatched =  Arrays.stream(SeverityLevel.values())
                 .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(incident.getSeverityLevel()));
        if (!isMatched) {
            throw new IllegalArgumentException("Invalid severity level. Must be LOW, MEDIUM, or HIGH.");
        }
    }

    private static void validateDate(IncidentDTO incident) {
        // Check for invalid date
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime incidentDate =  LocalDateTime.parse(incident.getIncidentDate());
        if (incidentDate.isBefore(thirtyDaysAgo) || incidentDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Incident date must be within the last 30 days and not in the future");
        }
    }

    private void validateExistingIncident(IncidentDTO incidentDTO){

        validateDate(incidentDTO);
        validateSeverity(incidentDTO);



    }

    private static void validateNewIncidentStatus(IncidentDTO incident) {
        // Check for OPEN STATUS for new incident
        if (incident.getStatus() !=null && !incident.getStatus().equalsIgnoreCase(Status.OPEN.name())) {
           throw new IllegalArgumentException("Status of new incident must be OPEN");
        }
    }
}
