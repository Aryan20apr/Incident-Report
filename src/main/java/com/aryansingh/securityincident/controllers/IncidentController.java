package com.aryansingh.securityincident.controllers;

import com.aryansingh.securityincident.models.dtos.IncidentDTO;
import com.aryansingh.securityincident.services.IncidentService;
import com.aryansingh.securityincident.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/incident")
@AllArgsConstructor
public class IncidentController {

    IncidentService incidentService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> newIncident(@Valid @RequestBody IncidentDTO incident) {

        String token = incidentService.createIncident(incident);

        return new ResponseEntity<>(new ApiResponse<>("Incident reported successfully",token), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<IncidentDTO>> updateIncident(@Valid @RequestBody IncidentDTO incident) {

       IncidentDTO incidentDTO = incidentService.updateIncident(incident);

        return new ResponseEntity<>(new ApiResponse<>("Incident updated successfully",incidentDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<IncidentDTO>> getIncident(@RequestParam String id) {

        IncidentDTO incidentDTO = incidentService.findIncidentById(id);

        return new ResponseEntity<>(new ApiResponse<>("Incident obtained !",incidentDTO), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<IncidentDTO>>> getIncidents(
            @RequestParam(required = false,name = "sv") String severity,
            @RequestParam(required = false,name = "sd") String startDate,
            @RequestParam(required = false,name = "ed") String endDate) {

      List<IncidentDTO> incidentDTOS =   incidentService.getAllIncidents(severity, startDate, endDate);

      return new ResponseEntity<>(new ApiResponse<>("Incidents obtained !",incidentDTOS), HttpStatus.OK);
    }
}
