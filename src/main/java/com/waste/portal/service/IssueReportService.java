package com.waste.portal.service;

import com.waste.portal.model.IssueReport;
import com.waste.portal.model.User;
import com.waste.portal.model.IssueStatus;
import com.waste.portal.repository.IssueReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IssueReportService {

    private final IssueReportRepository issueReportRepository;

    public IssueReportService(IssueReportRepository issueReportRepository) {
        this.issueReportRepository = issueReportRepository;
    }

    public Optional<IssueReport> findById(Long id) {
        return issueReportRepository.findById(id);
    }

    public List<IssueReport> findAll() {
        return issueReportRepository.findAll();
    }

    public List<IssueReport> findByCitizen(User citizen) {
        return issueReportRepository.findByCitizenOrderByReportedDateDesc(citizen);
    }

    public List<IssueReport> findByAssignedStaff(User staff) {
        return issueReportRepository.findByAssignedStaffOrderByReportedDateDesc(staff);
    }

    public List<IssueReport> findByStatus(IssueStatus status) {
        return issueReportRepository.findByStatus(status);
    }

    @Transactional
    public IssueReport createIssue(IssueReport issue) {
        issue.setReportedDate(LocalDateTime.now());
        issue.setStatus(IssueStatus.REPORTED);
        return issueReportRepository.save(issue);
    }

    @Transactional
    public IssueReport assignStaff(Long issueId, User staff) {
        IssueReport issue = issueReportRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found: " + issueId));
        issue.setAssignedStaff(staff);
        issue.setStatus(IssueStatus.INVESTIGATING);
        return issueReportRepository.save(issue);
    }

    @Transactional
    public IssueReport resolveIssue(Long issueId, String resolutionNotes) {
        IssueReport issue = issueReportRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found: " + issueId));
        issue.setResolutionNotes(resolutionNotes);
        issue.setStatus(IssueStatus.RESOLVED);
        return issueReportRepository.save(issue);
    }

    @Transactional
    public IssueReport save(IssueReport issue) {
        return issueReportRepository.save(issue);
    }
}
