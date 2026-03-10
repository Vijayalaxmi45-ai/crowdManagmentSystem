package com.crowd.mapper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // Pothole, Garbage, etc.
    private String state;
    private String city;
    private String area;
    private String description;

    private String severity; // Low, Medium, High
    private String status = "Open"; // Open, In Progress, Resolved

    private int confirms = 0;
    private int rejects = 0;
    private int priority = 0; // Calculated: (confirms - rejects) + severityScore

    @CreationTimestamp
    private LocalDateTime createdAt;
}
