package com.crowd.mapper.repository;

import com.crowd.mapper.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    // Fetch all problems sorted by priority (Highest first)
    List<Problem> findAllByOrderByPriorityDesc();

    // Fetch top 5 problems by priority
    List<Problem> findTop5ByOrderByPriorityDesc();

    // Count problems by city for dashboard
    long countByCity(String city);

    // Status and Severity counts
    long countByStatus(String status);

    long countBySeverity(String severity);

    // Find by status
    List<Problem> findByStatus(String status);
}
