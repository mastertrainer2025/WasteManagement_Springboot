package com.waste.portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "route_schedules")
public class RouteSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "waste_type", nullable = false)
    private WasteType wasteType;

    @Column(name = "scheduled_day", nullable = false)
    private String scheduledDay;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_staff_id", nullable = false)
    private User assignedStaff;

    private boolean active = true;

    public RouteSchedule() {}

    public RouteSchedule(Long id, String routeName, WasteType wasteType, String scheduledDay, User assignedStaff, boolean active) {
        this.id = id;
        this.routeName = routeName;
        this.wasteType = wasteType;
        this.scheduledDay = scheduledDay;
        this.assignedStaff = assignedStaff;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public WasteType getWasteType() { return wasteType; }
    public void setWasteType(WasteType wasteType) { this.wasteType = wasteType; }

    public String getScheduledDay() { return scheduledDay; }
    public void setScheduledDay(String scheduledDay) { this.scheduledDay = scheduledDay; }

    public User getAssignedStaff() { return assignedStaff; }
    public void setAssignedStaff(User assignedStaff) { this.assignedStaff = assignedStaff; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static RouteScheduleBuilder builder() {
        return new RouteScheduleBuilder();
    }

    public static class RouteScheduleBuilder {
        private Long id;
        private String routeName;
        private WasteType wasteType;
        private String scheduledDay;
        private User assignedStaff;
        private boolean active = true;

        public RouteScheduleBuilder id(Long id) { this.id = id; return this; }
        public RouteScheduleBuilder routeName(String routeName) { this.routeName = routeName; return this; }
        public RouteScheduleBuilder wasteType(WasteType wasteType) { this.wasteType = wasteType; return this; }
        public RouteScheduleBuilder scheduledDay(String scheduledDay) { this.scheduledDay = scheduledDay; return this; }
        public RouteScheduleBuilder assignedStaff(User assignedStaff) { this.assignedStaff = assignedStaff; return this; }
        public RouteScheduleBuilder active(boolean active) { this.active = active; return this; }

        public RouteSchedule build() {
            return new RouteSchedule(id, routeName, wasteType, scheduledDay, assignedStaff, active);
        }
    }
}
