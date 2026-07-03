package com.waste.portal.repository;

import com.waste.portal.model.IssueReport;
import com.waste.portal.model.User;
import com.waste.portal.model.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {
    List<IssueReport> findByCitizenOrderByReportedDateDesc(User citizen);
    List<IssueReport> findByAssignedStaffOrderByReportedDateDesc(User staff);
    List<IssueReport> findByStatus(IssueStatus status);
}
