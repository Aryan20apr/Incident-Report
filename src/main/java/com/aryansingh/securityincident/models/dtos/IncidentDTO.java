package com.aryansingh.securityincident.models.dtos;

import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.models.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class IncidentDTO {


    //private String incidentToken;

    @NotEmpty(message = "Title for incident cannot be empty.")
    @Size(min = 10, message = "Title must be at least 10 characters long.")
    private String title;

    @NotEmpty(message = "Description for incident cannot be empty.")
    private String description;

    @NotEmpty(message = "Severity Level of incident must be specified.")
    private String severityLevel;

    private String incidentDate;

    private Date updatedAt;

    @NotEmpty(message = "Status of incident must be specified.")
    private Status status;

    private String notes;

}
