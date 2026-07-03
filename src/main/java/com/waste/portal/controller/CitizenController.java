package com.waste.portal.controller;

import com.waste.portal.model.*;
import com.waste.portal.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/citizen")
public class CitizenController {

    private final UserService userService;
    private final PickupRequestService pickupRequestService;
    private final IssueReportService issueReportService;
    private final NotificationService notificationService;

    public CitizenController(UserService userService,
                             PickupRequestService pickupRequestService,
                             IssueReportService issueReportService,
                             NotificationService notificationService) {
        this.userService = userService;
        this.pickupRequestService = pickupRequestService;
        this.issueReportService = issueReportService;
        this.notificationService = notificationService;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userDetails.getUsername()));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User citizen = getAuthenticatedUser(userDetails);
        
        List<WastePickupRequest> pickups = pickupRequestService.findByCitizen(citizen);
        List<IssueReport> issues = issueReportService.findByCitizen(citizen);
        List<Notification> notifications = notificationService.findByUser(citizen);
        long unreadCount = notificationService.getUnreadCount(citizen);

        model.addAttribute("pickups", pickups);
        model.addAttribute("issues", issues);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("user", citizen);

        return "citizen/dashboard";
    }

    @GetMapping("/request")
    public String requestPickupForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User citizen = getAuthenticatedUser(userDetails);
        
        WastePickupRequest request = new WastePickupRequest();
        request.setPickupAddress(citizen.getAddress());
        
        model.addAttribute("pickupRequest", request);
        model.addAttribute("wasteTypes", WasteType.values());
        model.addAttribute("user", citizen);
        return "citizen/request";
    }

    @PostMapping("/request")
    public String submitPickupRequest(@ModelAttribute("pickupRequest") WastePickupRequest request, 
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User citizen = getAuthenticatedUser(userDetails);
        request.setCitizen(citizen);
        pickupRequestService.createRequest(request);
        return "redirect:/citizen/dashboard?pickupSuccess=true";
    }

    @GetMapping("/issue")
    public String reportIssueForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User citizen = getAuthenticatedUser(userDetails);
        
        model.addAttribute("issueReport", new IssueReport());
        model.addAttribute("issueTypes", IssueType.values());
        model.addAttribute("user", citizen);
        return "citizen/issue";
    }

    @PostMapping("/issue")
    public String submitIssueReport(@ModelAttribute("issueReport") IssueReport issue, 
                                    @AuthenticationPrincipal UserDetails userDetails) {
        User citizen = getAuthenticatedUser(userDetails);
        issue.setCitizen(citizen);
        issueReportService.createIssue(issue);
        return "redirect:/citizen/dashboard?issueSuccess=true";
    }

    @PostMapping("/notifications/read")
    public String markNotificationsRead(@AuthenticationPrincipal UserDetails userDetails) {
        User citizen = getAuthenticatedUser(userDetails);
        notificationService.markAllAsRead(citizen);
        return "redirect:/citizen/dashboard";
    }
}
