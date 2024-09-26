package com.aryansingh.securityincident.services;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;

import java.util.List;

public interface IncidentService {



    String createIncident(IncidentDTO incidentDTO);

    IncidentDTO updateIncident(IncidentDTO incidentDTO);

    List<IncidentDTO> getAllIncidents(); // TODO: Add optional filtering severity level and date range.

    IncidentDTO findIncidentById(String token);



}
