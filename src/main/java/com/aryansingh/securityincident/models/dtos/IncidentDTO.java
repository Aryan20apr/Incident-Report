package com.aryansingh.securityincident.models.dtos;

import com.aryansingh.securityincident.models.enums.Status;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class IncidentDTO {


    private String incidentToken;

    @NotEmpty(message = "Title for incident cannot be empty.")
    @Size(min = 10, message = "Title must be at least 10 characters long.")
    private String title;


    @NotEmpty(message = "Severity Level of incident must be specified.")
    private String severityLevel;

    @NotEmpty(message ="Date of incident cannot be empty. It must be ISO-8601 format")
    private String incidentDate;

    private String updatedAt;

    private String status;

    private String notes;

}
