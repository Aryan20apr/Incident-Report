package com.aryansingh.securityincident.services.impl;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.models.enums.Status;
import com.aryansingh.securityincident.repositories.IncidentRepository;
import com.aryansingh.securityincident.repositories.specifications.IncidentSpecifications;
import com.aryansingh.securityincident.services.IncidentService;
import com.aryansingh.securityincident.utils.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.channels.ScatteringByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .incidentDate(convertDateTime(incidentDTO.getIncidentDate()))
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

            Optional<Incident> optionalIncident = incidentRepository.findByIncidentToken(token);

            if(optionalIncident.isEmpty()){
                throw new IllegalArgumentException("Incident with given token does not exist");
            } else{

                Incident incident = optionalIncident.get();
                incident.setTitle(title);
                incident.setIncidentDate(convertDateTime(incidentDTO.getIncidentDate()));

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
    public List<IncidentDTO> getAllIncidents(String severity, String startDate, String endDate) {
        if(severity!=null) {
            validateSeverity(severity);
        }
        LocalDateTime startDateTime = startDate!=null ? convertDateTime(startDate):null;
        LocalDateTime endDateTime = endDate!=null ? convertDateTime(endDate):null;
        SeverityLevel sv = severity == null ? null: SeverityLevel.valueOf(severity);
////              List<Incident> incidents =   incidentRepository.findAll();
//        List<Incident> incidents = incidentRepository.findAllWithFilters(sv, startDateTime,endDateTime);
////        List<Incident> incidents = incidentRepository.findAllWithFilters(sv);

        Specification<Incident> spec = Specification
                .where(IncidentSpecifications.hasSeverityLevel(sv))
                .and(IncidentSpecifications.hasIncidentDateBetween(startDateTime, endDateTime));

        List<Incident> incidents =  incidentRepository.findAll(spec);

       return incidents.stream().map(IncidentServiceImpl::convertToDTO).toList();
    }

    private static LocalDateTime convertDateTime(String startDate) {

        if(startDate == null)
                throw new ApiException("Date cannot be empty");
        try {
           return LocalDateTime.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new ApiException("Invalid date format");
        }
    }

    @Override
    public IncidentDTO findIncidentById(String token) {

        Optional<Incident> optionalIncident = incidentRepository.findByIncidentToken(token);
        if(optionalIncident.isEmpty()){
            throw new ApiException("Incident with given token does not exist");
        } else{

            Incident incident = optionalIncident.get();
            return convertToDTO(incident);
        }
    }

    private static IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setTitle(incident.getTitle());
        incidentDTO.setIncidentDate(incident.getIncidentDate().toString());
        incidentDTO.setUpdatedAt(Optional.ofNullable(incident.getUpdatedAt())
                .map(LocalDateTime::toString)
                .orElse("N/A"));
        incidentDTO.setStatus(incident.getStatus().name());
        incidentDTO.setNotes(incident.getNotes());
        incidentDTO.setSeverityLevel(incident.getSeverityLevel().name());
        incidentDTO.setIncidentToken(incident.getIncidentToken());
        return incidentDTO;
    }

    private void validateIncident(IncidentDTO incident) {


        // Check for duplicate title
        if (incidentRepository.existsByTitle(incident.getTitle())) {
            throw new IllegalArgumentException("An incident with this title already exists");
        }
        validateSeverity(incident.getSeverityLevel());
        validateDate(incident);
    }

    private static void validateSeverity(String severity) {
        // Check for invalid Severity Level

        boolean isMatched =  Arrays.stream(SeverityLevel.values())
                 .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(severity.trim()));
        if (!isMatched) {
            throw new IllegalArgumentException("Invalid severity level. Must be LOW, MEDIUM, or HIGH.");
        }
    }

    private static void validateDate(IncidentDTO incident) {
        // Check for invalid date
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime incidentDate = convertDateTime(incident.getIncidentDate());
        if (incidentDate.isBefore(thirtyDaysAgo) || incidentDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Incident date must be within the last 30 days and not in the future");
        }
    }

    private void validateExistingIncident(IncidentDTO incidentDTO){

        if(incidentRepository.existsByTitleAndNotIncidentToken(incidentDTO.getTitle(),incidentDTO.getIncidentToken()))
            throw new IllegalArgumentException("Incident with given title already exists");

        validateDate(incidentDTO);
        validateSeverity(incidentDTO.getSeverityLevel());

    }

    private static void validateNewIncidentStatus(IncidentDTO incident) {
        // Check for OPEN STATUS for new incident
        if (incident.getStatus() !=null && !incident.getStatus().equalsIgnoreCase(Status.OPEN.name())) {
           throw new IllegalArgumentException("Status of new incident must be OPEN");
        }
    }
}
