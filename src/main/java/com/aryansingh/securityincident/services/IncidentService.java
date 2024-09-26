package com.aryansingh.securityincident.services;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;

import java.util.List;

public interface IncidentService {



    String createIncident(IncidentDTO incidentDTO);

    IncidentDTO updateIncident(IncidentDTO incidentDTO);

    List<IncidentDTO> getAllIncidents(String severity, String startDate, String endDate);

    IncidentDTO findIncidentById(String token);



}
