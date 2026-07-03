package com.waste.portal.controller;

import com.waste.portal.model.*;
import com.waste.portal.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PickupRequestService pickupRequestService;
    private final IssueReportService issueReportService;
    private final RouteScheduleService routeScheduleService;
    private final NotificationService notificationService;

    public AdminController(UserService userService,
                           PickupRequestService pickupRequestService,
                           IssueReportService issueReportService,
                           RouteScheduleService routeScheduleService,
                           NotificationService notificationService) {
        this.userService = userService;
        this.pickupRequestService = pickupRequestService;
        this.issueReportService = issueReportService;
        this.routeScheduleService = routeScheduleService;
        this.notificationService = notificationService;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userDetails.getUsername()));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = getAuthenticatedUser(userDetails);
        
        List<WastePickupRequest> allPickups = pickupRequestService.findAll();
        List<IssueReport> allIssues = issueReportService.findAll();
        List<User> allUsers = userService.findAll();

        long citizenCount = allUsers.stream().filter(u -> u.getRole() == Role.CITIZEN).count();
        long staffCount = allUsers.stream().filter(u -> u.getRole() == Role.STAFF).count();
        long pendingPickups = allPickups.stream().filter(p -> p.getStatus() == RequestStatus.PENDING).count();
        long activePickups = allPickups.stream().filter(p -> p.getStatus() == RequestStatus.ASSIGNED).count();
        long completedPickups = allPickups.stream().filter(p -> p.getStatus() == RequestStatus.COMPLETED).count();
        long unresolvedIssues = allIssues.stream().filter(i -> i.getStatus() != IssueStatus.RESOLVED).count();

        List<WastePickupRequest> pendingList = pickupRequestService.findByStatus(RequestStatus.PENDING);
        List<IssueReport> unresolvedList = allIssues.stream().filter(i -> i.getStatus() != IssueStatus.RESOLVED).collect(Collectors.toList());
        List<User> staffList = userService.findByRole(Role.STAFF);

        model.addAttribute("citizenCount", citizenCount);
        model.addAttribute("staffCount", staffCount);
        model.addAttribute("pendingPickupsCount", pendingPickups);
        model.addAttribute("activePickupsCount", activePickups);
        model.addAttribute("completedPickupsCount", completedPickups);
        model.addAttribute("unresolvedIssuesCount", unresolvedIssues);

        model.addAttribute("pendingPickups", pendingList);
        model.addAttribute("unresolvedIssues", unresolvedList);
        model.addAttribute("staffMembers", staffList);
        model.addAttribute("user", admin);

        return "admin/dashboard";
    }

    @PostMapping("/pickup/{id}/assign")
    public String assignPickup(@PathVariable("id") Long id,
                               @RequestParam("staffId") Long staffId,
                               @RequestParam("scheduledDate") String scheduledDateStr) {
        User staff = userService.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found: " + staffId));
        LocalDate scheduledDate = LocalDate.parse(scheduledDateStr);
        
        pickupRequestService.assignStaff(id, staff, scheduledDate);

        WastePickupRequest request = pickupRequestService.findById(id).get();
        notificationService.createNotification(
                "Pickup Request Scheduled",
                "Your waste pickup request has been assigned to staff " + staff.getName() + " for scheduled date: " + scheduledDate,
                request.getCitizen()
        );

        return "redirect:/admin/dashboard?success=pickup_assigned";
    }

    @PostMapping("/issue/{id}/assign")
    public String assignIssue(@PathVariable("id") Long id,
                             @RequestParam("staffId") Long staffId) {
        User staff = userService.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found: " + staffId));
        
        issueReportService.assignStaff(id, staff);

        IssueReport issue = issueReportService.findById(id).get();
        notificationService.createNotification(
                "Issue Under Investigation",
                "Your reported issue (" + issue.getIssueType() + ") has been assigned to staff " + staff.getName() + " for investigation.",
                issue.getCitizen()
        );

        return "redirect:/admin/dashboard?success=issue_assigned";
    }

    @GetMapping("/users")
    public String usersPanel(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = getAuthenticatedUser(userDetails);
        
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", admin);
        return "admin/users";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute("newUser") User user) {
        try {
            userService.createUser(user);
            return "redirect:/admin/users?success=user_created";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/users?error=email_exists";
        }
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable("id") Long id) {
        User target = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        target.setEnabled(!target.isEnabled());
        userService.save(target);
        return "redirect:/admin/users?success=status_updated";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users?success=user_deleted";
    }

    @GetMapping("/routes")
    public String routesPanel(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = getAuthenticatedUser(userDetails);
        
        List<RouteSchedule> routes = routeScheduleService.findAll();
        List<User> staffList = userService.findByRole(Role.STAFF);

        model.addAttribute("routes", routes);
        model.addAttribute("staffMembers", staffList);
        model.addAttribute("newRoute", new RouteSchedule());
        model.addAttribute("wasteTypes", WasteType.values());
        model.addAttribute("user", admin);
        return "admin/routes";
    }

    @PostMapping("/routes/create")
    public String createRoute(@ModelAttribute("newRoute") RouteSchedule schedule,
                              @RequestParam("staffId") Long staffId) {
        User staff = userService.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found: " + staffId));
        schedule.setAssignedStaff(staff);
        schedule.setActive(true);
        routeScheduleService.save(schedule);
        return "redirect:/admin/routes?success=route_created";
    }

    @PostMapping("/routes/{id}/toggle-status")
    public String toggleRouteStatus(@PathVariable("id") Long id) {
        RouteSchedule route = routeScheduleService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + id));
        route.setActive(!route.isActive());
        routeScheduleService.save(route);
        return "redirect:/admin/routes?success=route_updated";
    }

    @PostMapping("/routes/{id}/delete")
    public String deleteRoute(@PathVariable("id") Long id) {
        routeScheduleService.deleteById(id);
        return "redirect:/admin/routes?success=route_deleted";
    }

    @GetMapping("/reports")
    public String reportsPanel(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = getAuthenticatedUser(userDetails);
        
        List<WastePickupRequest> allPickups = pickupRequestService.findAll();
        List<IssueReport> allIssues = issueReportService.findAll();

        long organicCount = allPickups.stream().filter(p -> p.getWasteType() == WasteType.ORGANIC && p.getStatus() == RequestStatus.COMPLETED).count();
        long recyclableCount = allPickups.stream().filter(p -> p.getWasteType() == WasteType.RECYCLABLE && p.getStatus() == RequestStatus.COMPLETED).count();
        long hazardousCount = allPickups.stream().filter(p -> p.getWasteType() == WasteType.HAZARDOUS && p.getStatus() == RequestStatus.COMPLETED).count();
        long bulkyCount = allPickups.stream().filter(p -> p.getWasteType() == WasteType.BULKY && p.getStatus() == RequestStatus.COMPLETED).count();

        long resolvedIssues = allIssues.stream().filter(i -> i.getStatus() == IssueStatus.RESOLVED).count();
        long reportedIssues = allIssues.stream().filter(i -> i.getStatus() == IssueStatus.REPORTED).count();
        long investigatingIssues = allIssues.stream().filter(i -> i.getStatus() == IssueStatus.INVESTIGATING).count();

        model.addAttribute("organicCount", organicCount);
        model.addAttribute("recyclableCount", recyclableCount);
        model.addAttribute("hazardousCount", hazardousCount);
        model.addAttribute("bulkyCount", bulkyCount);

        model.addAttribute("resolvedIssues", resolvedIssues);
        model.addAttribute("reportedIssues", reportedIssues);
        model.addAttribute("investigatingIssues", investigatingIssues);
        model.addAttribute("user", admin);

        return "admin/reports";
    }

    @PostMapping("/broadcast")
    public String broadcastAnnouncement(@RequestParam("title") String title,
                                        @RequestParam("content") String content) {
        notificationService.createNotification(title, content, null);
        return "redirect:/admin/dashboard?success=broadcast_sent";
    }
}
