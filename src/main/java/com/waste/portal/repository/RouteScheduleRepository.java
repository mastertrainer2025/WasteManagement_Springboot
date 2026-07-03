package com.waste.portal.repository;

import com.waste.portal.model.RouteSchedule;
import com.waste.portal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteScheduleRepository extends JpaRepository<RouteSchedule, Long> {
    List<RouteSchedule> findByAssignedStaffAndActiveTrue(User staff);
    List<RouteSchedule> findByActiveTrue();
}
