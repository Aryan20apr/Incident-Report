package com.aryansingh.securityincident.controllers;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.services.IncidentService;
import com.aryansingh.securityincident.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incident/api/v1")
@AllArgsConstructor
public class IncidentController {

    IncidentService incidentService;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<String>> newIncident(@Valid @RequestBody IncidentDTO incident) {

        String token = incidentService.createIncident(incident);

        return new ResponseEntity<>(new ApiResponse<>("Incident reported successfully",token), HttpStatus.CREATED);
    }
}
