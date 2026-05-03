package com.crowd.mapper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "crowd_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrowdLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    private int currentCapacity;
    private int maxCapacity;
    private String status; // "Normal", "Crowded", "Full"
}
