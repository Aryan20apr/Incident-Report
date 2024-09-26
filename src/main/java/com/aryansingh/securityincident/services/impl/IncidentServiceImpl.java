package com.aryansingh.securityincident.services.impl;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.services.IncidentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Override
    public String createIncident(IncidentDTO incidentDTO) {
        return "";
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
}
