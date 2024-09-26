package com.aryansingh.securityincident.models.entities;

import com.aryansingh.securityincident.models.enums.SeverityLevel;
import com.aryansingh.securityincident.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String incidentToken;

    @Column(unique = true,nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private SeverityLevel severityLevel;


    private LocalDateTime incidentDate;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    private Status status;

    private String notes;

    @PrePersist
    public void generateToken() {
        if (this.incidentToken == null) {
            this.incidentToken = UUID.randomUUID().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Incident incident))
            return false;
        return Objects.equals(getId(), incident.getId()) && Objects.equals(getIncidentToken(),
                incident.getIncidentToken()) && Objects.equals(getTitle(), incident.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIncidentToken(), getTitle());
    }
}

