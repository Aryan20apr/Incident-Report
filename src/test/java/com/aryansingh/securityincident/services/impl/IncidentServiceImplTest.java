package com.aryansingh.securityincident.services.impl;



import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.models.enums.Status;
import com.aryansingh.securityincident.repositories.IncidentRepository;
import com.aryansingh.securityincident.utils.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IncidentServiceImplTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for createIncident method
    @Test
    void createIncident_ValidIncident_ShouldSucceed() {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setTitle("Test Incident");
        incidentDTO.setIncidentDate(LocalDateTime.now().toString());
        incidentDTO.setSeverityLevel("HIGH");
        incidentDTO.setNotes("Test notes");


        when(incidentRepository.existsByTitle(anyString())).thenReturn(false);
        when(incidentRepository.save(any(Incident.class))).thenAnswer(invocation -> {
            Incident savedIncident = invocation.getArgument(0);
            savedIncident.setIncidentToken("TEST-TOKEN");
            return savedIncident;
        });

        String result = incidentService.createIncident(incidentDTO);

        assertNotNull(result);
        assertEquals("TEST-TOKEN", result);
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void createIncident_DuplicateTitle_ShouldThrowException() {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setTitle("Duplicate Title");
        incidentDTO.setIncidentDate(LocalDateTime.now().toString());
        incidentDTO.setSeverityLevel("MEDIUM");

        when(incidentRepository.existsByTitle(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> incidentService.createIncident(incidentDTO));
    }

    @Test
    void createIncident_InvalidSeverityLevel_ShouldThrowException() {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setTitle("Test Incident");
        incidentDTO.setIncidentDate(LocalDateTime.now().toString());
        incidentDTO.setSeverityLevel("INVALID");

        assertThrows(IllegalArgumentException.class, () -> incidentService.createIncident(incidentDTO));
    }

    @Test
    void createIncident_InvalidDate_ShouldThrowException() {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setTitle("Test Incident");
        incidentDTO.setIncidentDate(LocalDateTime.now().minusDays(31).toString());
        incidentDTO.setSeverityLevel("HIGH");

        assertThrows(IllegalArgumentException.class, () -> incidentService.createIncident(incidentDTO));
    }

    // Tests for updateIncident method
    @Test
    void updateIncident_ValidUpdate_ShouldSucceed() {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setIncidentToken("TEST-TOKEN");
        incidentDTO.setTitle("Updated Incident");
        incidentDTO.setIncidentDate(LocalDateTime.now().toString());
        incidentDTO.setSeverityLevel("HIGH");
        incidentDTO.setStatus("IN_PROGRESS");
        incidentDTO.setNotes("Updated notes");

        Incident existingIncident = new Incident();
        existingIncident.setIncidentToken("TEST-TOKEN");
        existingIncident.setTitle("Original Incident");
        existingIncident.setUpdatedAt(LocalDateTime.now());

        when(incidentRepository.findByIncidentToken("TEST-TOKEN")).thenReturn(Optional.of(existingIncident));
        when(incidentRepository.existsByTitleAndNotIncidentToken(anyString(), anyString())).thenReturn(false);
        when(incidentRepository.save(any(Incident.class))).thenReturn(existingIncident);

        IncidentDTO result = incidentService.updateIncident(incidentDTO);

        assertNotNull(result);
        assertEquals("Updated Incident", result.getTitle());
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(incidentRepository).save(any(Incident.class));
    }

    @Test
    void updateIncident_NonExistentIncident_ShouldThrowException() {
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setIncidentToken("NON-EXISTENT-TOKEN");

        when(incidentRepository.findByIncidentToken("NON-EXISTENT-TOKEN")).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> incidentService.updateIncident(incidentDTO));
    }

    // Tests for getAllIncidents method
    @Test
    void getAllIncidents_WithFilters_ShouldReturnFilteredList() {
        String severity = "HIGH";
        String startDate = LocalDateTime.now().minusDays(7).toString();
        String endDate = LocalDateTime.now().toString();

        Incident incident1 = new Incident();
        incident1.setTitle("Incident 1");
        incident1.setSeverityLevel(SeverityLevel.HIGH);
        incident1.setIncidentDate(LocalDateTime.now().minusDays(5));
        incident1.setStatus(Status.OPEN);

        Incident incident2 = new Incident();
        incident2.setTitle("Incident 2");
        incident2.setSeverityLevel(SeverityLevel.HIGH);
        incident2.setIncidentDate(LocalDateTime.now().minusDays(3));
        incident2.setStatus(Status.IN_PROGRESS);

        List<Incident> mockIncidents = Arrays.asList(incident1, incident2);

        when(incidentRepository.findAll(any(Specification.class))).thenReturn(mockIncidents);

        List<IncidentDTO> result = incidentService.getAllIncidents(severity, startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Incident 1", result.get(0).getTitle());
        assertEquals("Incident 2", result.get(1).getTitle());
        assertEquals("HIGH", result.get(0).getSeverityLevel());
        assertEquals("HIGH", result.get(1).getSeverityLevel());
        verify(incidentRepository).findAll(any(Specification.class));
    }

    @Test
    void getAllIncidents_InvalidSeverity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> incidentService.getAllIncidents("INVALID", null, null));
    }

    @Test
    void getAllIncidents_InvalidDateFormat_ShouldThrowException() {
        assertThrows(ApiException.class, () -> incidentService.getAllIncidents(null, "invalid-date", null));
    }

    // Tests for findIncidentById method
    @Test
    void findIncidentById_ExistingIncident_ShouldReturnIncident() {
        String token = "TEST-TOKEN";
        Incident incident = new Incident();
        incident.setIncidentToken(token);
        incident.setTitle("Test Incident");
        incident.setSeverityLevel(SeverityLevel.HIGH);
        incident.setStatus(Status.OPEN);
        incident.setIncidentDate(LocalDateTime.now());
        incident.setUpdatedAt(LocalDateTime.now());

        when(incidentRepository.findByIncidentToken(token)).thenReturn(Optional.of(incident));

        IncidentDTO result = incidentService.findIncidentById(token);

        assertNotNull(result);
        assertEquals("Test Incident", result.getTitle());
        assertEquals("HIGH", result.getSeverityLevel());
        assertEquals("OPEN", result.getStatus());
    }

    @Test
    void findIncidentById_NonExistentIncident_ShouldThrowException() {
        String token = "NON-EXISTENT-TOKEN";
        when(incidentRepository.findByIncidentToken(token)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> incidentService.findIncidentById(token));
    }
}
