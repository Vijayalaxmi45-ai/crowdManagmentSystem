package com.crowd.mapper.repository;

import com.crowd.mapper.model.CrowdLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrowdLocationRepository extends JpaRepository<CrowdLocation, Long> {
}
