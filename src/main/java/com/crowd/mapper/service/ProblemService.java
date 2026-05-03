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
            problem.setStatus("PENDING");
        }

        problemRepository.save(problem);
    }

    public void verifyProblem(Long id, boolean isValid, String remarks, String proofImage) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            p.setStatus(isValid ? "VERIFIED" : "REJECTED");
            p.setRemarks(remarks);
            if (proofImage != null) p.setVerificationProofImage(proofImage);
            problemRepository.save(p);
        }
    }

    public void approveProblem(Long id, boolean isApproved) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            p.setStatus(isApproved ? "APPROVED" : "REJECTED");
            problemRepository.save(p);
        }
    }

    public void resolveProblem(Long id, String proofImage) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            p.setStatus("RESOLVED");
            if (proofImage != null) p.setResolutionProofImage(proofImage);
            problemRepository.save(p);
        }
    }

    public void assignToStaff(Long id, Long staffId) {
        // Mock assignment logic - in a real app, find user by id
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            // p.setAssignedStaff(staffService.findById(staffId));
            problemRepository.save(p);
        }
    }

    public void updateStatus(Long id, String status) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isPresent()) {
            Problem p = optionalProblem.get();
            p.setStatus(status);
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

    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }

    public long getTotalVerifications() {
        return getAllProblemsSorted().stream()
                .mapToLong(p -> p.getConfirms() + p.getRejects())
                .sum();
    }
}
