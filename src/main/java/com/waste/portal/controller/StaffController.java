package com.waste.portal.controller;

import com.waste.portal.model.*;
import com.waste.portal.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final UserService userService;
    private final PickupRequestService pickupRequestService;
    private final IssueReportService issueReportService;
    private final RouteScheduleService routeScheduleService;
    private final NotificationService notificationService;

    public StaffController(UserService userService,
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
        User staff = getAuthenticatedUser(userDetails);

        List<RouteSchedule> schedules = routeScheduleService.findByAssignedStaff(staff);
        List<WastePickupRequest> pickups = pickupRequestService.findByAssignedStaff(staff);
        List<IssueReport> issues = issueReportService.findByAssignedStaff(staff);

        model.addAttribute("schedules", schedules);
        model.addAttribute("pickups", pickups);
        model.addAttribute("issues", issues);
        model.addAttribute("user", staff);

        return "staff/dashboard";
    }

    @GetMapping("/pickup/{id}/complete")
    public String completePickupForm(@PathVariable("id") Long id, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        User staff = getAuthenticatedUser(userDetails);
        WastePickupRequest request = pickupRequestService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));

        if (request.getAssignedStaff() == null || !request.getAssignedStaff().getId().equals(staff.getId())) {
            return "redirect:/staff/dashboard?error=unauthorized";
        }

        model.addAttribute("pickup", request);
        model.addAttribute("user", staff);
        return "staff/update-pickup";
    }

    @PostMapping("/pickup/{id}/complete")
    public String completePickup(@PathVariable("id") Long id,
            @RequestParam("collectionProofNotes") String proofNotes,
            @RequestParam("collectionProofImage") String proofImage,
            @AuthenticationPrincipal UserDetails userDetails) {
        User staff = getAuthenticatedUser(userDetails);
        WastePickupRequest request = pickupRequestService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));

        if (request.getAssignedStaff() == null || !request.getAssignedStaff().getId().equals(staff.getId())) {
            return "redirect:/staff/dashboard?error=unauthorized";
        }

        pickupRequestService.completeRequest(id, proofNotes, proofImage);

        notificationService.createNotification(
                "Pickup Request Completed",
                "Your pickup request for " + request.getWasteType() + " waste has been completed by " + staff.getName()
                        + ".",
                request.getCitizen());

        return "redirect:/staff/dashboard?success=pickup_completed";
    }

    @PostMapping("/issue/{id}/resolve")
    public String resolveIssue(@PathVariable("id") Long id,
            @RequestParam("resolutionNotes") String resolutionNotes,
            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(">>> resolveIssue called for ID: " + id + ", notes: " + resolutionNotes);
        User staff = getAuthenticatedUser(userDetails);
        IssueReport issue = issueReportService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found: " + id));

        System.out.println(">>> Issue found in DB. ID: " + issue.getId() + ", Assigned staff: "
                + (issue.getAssignedStaff() != null ? issue.getAssignedStaff().getEmail() : "null"));

        if (issue.getAssignedStaff() == null || !issue.getAssignedStaff().getId().equals(staff.getId())) {
            System.out.println(">>> Unauthorized! Staff ID: " + staff.getId() + ", Assigned: "
                    + (issue.getAssignedStaff() != null ? issue.getAssignedStaff().getId() : "null"));
            return "redirect:/staff/dashboard?error=unauthorized";
        }

        issueReportService.resolveIssue(id, resolutionNotes);
        System.out.println(">>> Issue status set to RESOLVED in database.");

        notificationService.createNotification(
                "Issue Resolved",
                "The issue you reported (" + issue.getIssueType() + ") has been resolved. Notes: " + resolutionNotes,
                issue.getCitizen());
        System.out.println(">>> Notification sent to citizen: " + issue.getCitizen().getEmail());

        return "redirect:/staff/dashboard?success=issue_resolved";
    }
}
