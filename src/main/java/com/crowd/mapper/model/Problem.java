package com.crowd.mapper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    // --- New Features for Real-Time Smart System Workflow --- //
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private User assignedStaff;

    // Workflow: PENDING -> APPROVED -> ASSIGNED -> IN_PROGRESS -> RESOLVED
    private String workflowStatus = "PENDING";
    
    @Column(length = 1000)
    private String remarks;

    private String imagePath; // Path to user uploaded proof
    private String resolutionProofImage; // Path to staff uploaded resolution proof
}
