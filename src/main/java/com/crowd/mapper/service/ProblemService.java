package com.crowd.mapper.service;

import com.crowd.mapper.model.Problem;
import com.crowd.mapper.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    public List<Problem> getAllProblemsSorted() {
        return problemRepository.findAllByOrderByPriorityDesc();
    }

    public List<Problem> getTop5Problems() {
        return problemRepository.findTop5ByOrderByPriorityDesc();
    }

    public void saveProblem(Problem problem) {
        // New problems start with 0 confirm/reject
        problem.setConfirms(0);
        problem.setRejects(0);

        // Initial priority based on severity
        int severityScore = getSeverityScore(problem.getSeverity());
        problem.setPriority(severityScore);

        if (problem.getStatus() == null) {
            problem.setStatus("Open");
        }

        problemRepository.save(problem);
    }

    public void voteProblem(Long id, boolean isConfirm) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            if (isConfirm) {
                p.setConfirms(p.getConfirms() + 1);
            } else {
                p.setRejects(p.getRejects() + 1);
            }
            // Recalculate priority: (Confirms - Rejects) + SeverityScore
            int severityScore = getSeverityScore(p.getSeverity());
            p.setPriority((p.getConfirms() - p.getRejects()) + severityScore);

            problemRepository.save(p);
        }
    }

    private int getSeverityScore(String severity) {
        if ("High".equalsIgnoreCase(severity))
            return 10;
        if ("Medium".equalsIgnoreCase(severity))
            return 5;
        return 0; // Low
    }

    public long getTotalProblems() {
        return problemRepository.count();
    }

    public long getCountByStatus(String status) {
        return problemRepository.countByStatus(status);
    }

    public long getCountBySeverity(String severity) {
        return problemRepository.countBySeverity(severity);
    }

    public void updateStatus(Long id, String status) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            p.setStatus(status);
            problemRepository.save(p);
        }
    }

    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }

    public long getTotalVerifications() {
        return getAllProblemsSorted().stream()
                .mapToLong(p -> p.getConfirms() + p.getRejects())
                .sum();
    }
}
