package com.waste.portal.service;

import com.waste.portal.model.RouteSchedule;
import com.waste.portal.model.User;
import com.waste.portal.repository.RouteScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RouteScheduleService {

    private final RouteScheduleRepository routeScheduleRepository;

    public RouteScheduleService(RouteScheduleRepository routeScheduleRepository) {
        this.routeScheduleRepository = routeScheduleRepository;
    }

    public Optional<RouteSchedule> findById(Long id) {
        return routeScheduleRepository.findById(id);
    }

    public List<RouteSchedule> findAll() {
        return routeScheduleRepository.findAll();
    }

    public List<RouteSchedule> findByAssignedStaff(User staff) {
        return routeScheduleRepository.findByAssignedStaffAndActiveTrue(staff);
    }

    public List<RouteSchedule> findActiveRoutes() {
        return routeScheduleRepository.findByActiveTrue();
    }

    @Transactional
    public RouteSchedule save(RouteSchedule schedule) {
        return routeScheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteById(Long id) {
        routeScheduleRepository.deleteById(id);
    }
}
