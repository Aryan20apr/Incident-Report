package com.aryansingh.securityincident.models.dtos;

import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.models.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IncidentDTO {


    //private String incidentToken;

    @NotEmpty(message = "Title for incident cannot be empty.")
    private String title;

    @NotEmpty(message = "Description for incident cannot be empty.")
    private String description;

    @NotEmpty(message = "Severity Level of incident must be specified.")
    private SeverityLevel severityLevel;

    private Date incidentDate;

    private Date updatedAt;

    @NotEmpty(message = "Status of incident must be specified.")
    private Status status;

    private String notes;

}
