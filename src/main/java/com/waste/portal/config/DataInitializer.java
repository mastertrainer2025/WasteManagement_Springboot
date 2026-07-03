package com.waste.portal.config;

import com.waste.portal.model.*;
import com.waste.portal.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PickupRequestService pickupRequestService;
    private final IssueReportService issueReportService;
    private final RouteScheduleService routeScheduleService;
    private final NotificationService notificationService;

    public DataInitializer(UserService userService,
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

    @Override
    public void run(String... args) throws Exception {
        if (userService.findAll().isEmpty()) {
            User admin = User.builder()
                    .email("admin@waste.com")
                    .password("admin123")
                    .name("Municipality Admin")
                    .phone("+1234567890")
                    .address("City Hall, Sector 1")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userService.createUser(admin);

            User citizen = User.builder()
                    .email("citizen@waste.com")
                    .password("citizen123")
                    .name("John Doe")
                    .phone("+1987654321")
                    .address("123 Eco Street, Green Zone")
                    .role(Role.CITIZEN)
                    .enabled(true)
                    .build();
            User savedCitizen = userService.createUser(citizen);

            User staff = User.builder()
                    .email("staff@waste.com")
                    .password("staff123")
                    .name("Robert Trucker")
                    .phone("+1555666777")
                    .address("Depot 5, West End")
                    .role(Role.STAFF)
                    .enabled(true)
                    .build();
            User savedStaff = userService.createUser(staff);

            RouteSchedule route1 = RouteSchedule.builder()
                    .routeName("Route Alpha - Green Zone")
                    .wasteType(WasteType.ORGANIC)
                    .scheduledDay("Monday")
                    .assignedStaff(savedStaff)
                    .active(true)
                    .build();
            routeScheduleService.save(route1);

            RouteSchedule route2 = RouteSchedule.builder()
                    .routeName("Route Beta - Commercial Area")
                    .wasteType(WasteType.RECYCLABLE)
                    .scheduledDay("Wednesday")
                    .assignedStaff(savedStaff)
                    .active(true)
                    .build();
            routeScheduleService.save(route2);

            WastePickupRequest request1 = WastePickupRequest.builder()
                    .citizen(savedCitizen)
                    .wasteType(WasteType.BULKY)
                    .pickupAddress("123 Eco Street, Green Zone")
                    .requestedDate(LocalDateTime.now().minusDays(5))
                    .scheduledDate(LocalDate.now().minusDays(3))
                    .assignedStaff(savedStaff)
                    .status(RequestStatus.COMPLETED)
                    .notes("Old wooden sofa set.")
                    .collectionProofNotes("Collected successfully. Cleaned the curb.")
                    .collectionProofImage("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='100' height='100' viewBox='0 0 100 100'><rect width='100' height='100' fill='%23e8f5e9'/><text x='50%25' y='50%25' dominant-baseline='middle' text-anchor='middle' font-family='sans-serif' font-size='10' fill='%232e7d32'>Pickup Done</text></svg>")
                    .build();
            pickupRequestService.save(request1);

            WastePickupRequest request2 = WastePickupRequest.builder()
                    .citizen(savedCitizen)
                    .wasteType(WasteType.HAZARDOUS)
                    .pickupAddress("123 Eco Street, Green Zone")
                    .requestedDate(LocalDateTime.now().minusDays(1))
                    .status(RequestStatus.PENDING)
                    .notes("Expired paints and batteries.")
                    .build();
            pickupRequestService.save(request2);

            IssueReport issue1 = IssueReport.builder()
                    .citizen(savedCitizen)
                    .issueType(IssueType.OVERFILLED_BIN)
                    .description("The main community bin at Sector 3 junction is overflowing and smell is spreading.")
                    .location("Sector 3 Junction, Near Post Office")
                    .status(IssueStatus.REPORTED)
                    .reportedDate(LocalDateTime.now().minusDays(2))
                    .build();
            issueReportService.createIssue(issue1);

            notificationService.createNotification(
                    "Welcome to Eco-Waste Portal!",
                    "You can now request waste pickups, report municipal waste issues, and track collection schedules directly from your citizen dashboard.",
                    savedCitizen
            );

            notificationService.createNotification(
                    "Waste Segregation Tip: Organic Waste",
                    "Always segregate dry and wet waste. Organic waste (vegetable peels, leftover food) should be disposed of in green bins for composting.",
                    null
            );

            notificationService.createNotification(
                    "Recycling Awareness: Plastics",
                    "Rinse plastic containers before placing them in the blue bins. Wet/soiled plastic cannot be recycled effectively.",
                    null
            );

            System.out.println(">>> Database initialization complete! Seeded Admin, Citizen, Staff, Routes, Pickups, and Notifications. <<<");
        }
    }
}
