package com.aryansingh.securityincident.repositories.specifications;

import com.aryansingh.securityincident.models.entities.Incident;
import com.aryansingh.securityincident.models.enums.SeverityLevel;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class IncidentSpecifications {


    public static Specification<Incident> hasSeverityLevel(SeverityLevel severityLevel) {
        return (root, query, builder) -> {
            if (severityLevel == null) {
                return builder.conjunction(); // No filtering on severity if null
            }
            return builder.equal(root.get("severityLevel"), severityLevel);
        };
    }

    public static Specification<Incident> hasIncidentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> {
            if (startDate != null && endDate != null) {
                return builder.between(root.get("incidentDate"), startDate, endDate);
            } else if (startDate != null) {
                return builder.greaterThanOrEqualTo(root.get("incidentDate"), startDate);
            } else if (endDate != null) {
                return builder.lessThanOrEqualTo(root.get("incidentDate"), endDate);
            }
            return builder.conjunction(); // No date filtering if both are null
        };
    }

}
