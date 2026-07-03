package com.waste.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_reports")
public class IssueReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "citizen_id", nullable = false)
    private User citizen;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type", nullable = false)
    private IssueType issueType;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status;

    @Column(name = "reported_date", nullable = false)
    private LocalDateTime reportedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private User assignedStaff;

    @Column(name = "resolution_notes", length = 1000)
    private String resolutionNotes;

    public IssueReport() {}

    public IssueReport(Long id, User citizen, IssueType issueType, String description, String location, IssueStatus status, LocalDateTime reportedDate, User assignedStaff, String resolutionNotes) {
        this.id = id;
        this.citizen = citizen;
        this.issueType = issueType;
        this.description = description;
        this.location = location;
        this.status = status;
        this.reportedDate = reportedDate;
        this.assignedStaff = assignedStaff;
        this.resolutionNotes = resolutionNotes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCitizen() { return citizen; }
    public void setCitizen(User citizen) { this.citizen = citizen; }

    public IssueType getIssueType() { return issueType; }
    public void setIssueType(IssueType issueType) { this.issueType = issueType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public LocalDateTime getReportedDate() { return reportedDate; }
    public void setReportedDate(LocalDateTime reportedDate) { this.reportedDate = reportedDate; }

    public User getAssignedStaff() { return assignedStaff; }
    public void setAssignedStaff(User assignedStaff) { this.assignedStaff = assignedStaff; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public static IssueReportBuilder builder() {
        return new IssueReportBuilder();
    }

    public static class IssueReportBuilder {
        private Long id;
        private User citizen;
        private IssueType issueType;
        private String description;
        private String location;
        private IssueStatus status;
        private LocalDateTime reportedDate;
        private User assignedStaff;
        private String resolutionNotes;

        public IssueReportBuilder id(Long id) { this.id = id; return this; }
        public IssueReportBuilder citizen(User citizen) { this.citizen = citizen; return this; }
        public IssueReportBuilder issueType(IssueType issueType) { this.issueType = issueType; return this; }
        public IssueReportBuilder description(String description) { this.description = description; return this; }
        public IssueReportBuilder location(String location) { this.location = location; return this; }
        public IssueReportBuilder status(IssueStatus status) { this.status = status; return this; }
        public IssueReportBuilder reportedDate(LocalDateTime reportedDate) { this.reportedDate = reportedDate; return this; }
        public IssueReportBuilder assignedStaff(User assignedStaff) { this.assignedStaff = assignedStaff; return this; }
        public IssueReportBuilder resolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; return this; }

        public IssueReport build() {
            return new IssueReport(id, citizen, issueType, description, location, status, reportedDate, assignedStaff, resolutionNotes);
        }
    }
}
