package com.waste.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste_pickup_requests")
public class WastePickupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "citizen_id", nullable = false)
    private User citizen;

    @Enumerated(EnumType.STRING)
    @Column(name = "waste_type", nullable = false)
    private WasteType wasteType;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "requested_date", nullable = false)
    private LocalDateTime requestedDate;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private User assignedStaff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private String notes;

    @Column(name = "collection_proof_notes", length = 1000)
    private String collectionProofNotes;

    @Lob
    @Column(name = "collection_proof_image", columnDefinition = "LONGTEXT")
    private String collectionProofImage;

    public WastePickupRequest() {}

    public WastePickupRequest(Long id, User citizen, WasteType wasteType, String pickupAddress, LocalDateTime requestedDate, LocalDate scheduledDate, User assignedStaff, RequestStatus status, String notes, String collectionProofNotes, String collectionProofImage) {
        this.id = id;
        this.citizen = citizen;
        this.wasteType = wasteType;
        this.pickupAddress = pickupAddress;
        this.requestedDate = requestedDate;
        this.scheduledDate = scheduledDate;
        this.assignedStaff = assignedStaff;
        this.status = status;
        this.notes = notes;
        this.collectionProofNotes = collectionProofNotes;
        this.collectionProofImage = collectionProofImage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCitizen() { return citizen; }
    public void setCitizen(User citizen) { this.citizen = citizen; }

    public WasteType getWasteType() { return wasteType; }
    public void setWasteType(WasteType wasteType) { this.wasteType = wasteType; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public LocalDateTime getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDateTime requestedDate) { this.requestedDate = requestedDate; }

    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    public User getAssignedStaff() { return assignedStaff; }
    public void setAssignedStaff(User assignedStaff) { this.assignedStaff = assignedStaff; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCollectionProofNotes() { return collectionProofNotes; }
    public void setCollectionProofNotes(String collectionProofNotes) { this.collectionProofNotes = collectionProofNotes; }

    public String getCollectionProofImage() { return collectionProofImage; }
    public void setCollectionProofImage(String collectionProofImage) { this.collectionProofImage = collectionProofImage; }

    public static WastePickupRequestBuilder builder() {
        return new WastePickupRequestBuilder();
    }

    public static class WastePickupRequestBuilder {
        private Long id;
        private User citizen;
        private WasteType wasteType;
        private String pickupAddress;
        private LocalDateTime requestedDate;
        private LocalDate scheduledDate;
        private User assignedStaff;
        private RequestStatus status;
        private String notes;
        private String collectionProofNotes;
        private String collectionProofImage;

        public WastePickupRequestBuilder id(Long id) { this.id = id; return this; }
        public WastePickupRequestBuilder citizen(User citizen) { this.citizen = citizen; return this; }
        public WastePickupRequestBuilder wasteType(WasteType wasteType) { this.wasteType = wasteType; return this; }
        public WastePickupRequestBuilder pickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; return this; }
        public WastePickupRequestBuilder requestedDate(LocalDateTime requestedDate) { this.requestedDate = requestedDate; return this; }
        public WastePickupRequestBuilder scheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; return this; }
        public WastePickupRequestBuilder assignedStaff(User assignedStaff) { this.assignedStaff = assignedStaff; return this; }
        public WastePickupRequestBuilder status(RequestStatus status) { this.status = status; return this; }
        public WastePickupRequestBuilder notes(String notes) { this.notes = notes; return this; }
        public WastePickupRequestBuilder collectionProofNotes(String collectionProofNotes) { this.collectionProofNotes = collectionProofNotes; return this; }
        public WastePickupRequestBuilder collectionProofImage(String collectionProofImage) { this.collectionProofImage = collectionProofImage; return this; }

        public WastePickupRequest build() {
            return new WastePickupRequest(id, citizen, wasteType, pickupAddress, requestedDate, scheduledDate, assignedStaff, status, notes, collectionProofNotes, collectionProofImage);
        }
    }
}
