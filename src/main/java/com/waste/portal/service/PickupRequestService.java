package com.waste.portal.service;

import com.waste.portal.model.WastePickupRequest;
import com.waste.portal.model.User;
import com.waste.portal.model.RequestStatus;
import com.waste.portal.repository.WastePickupRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PickupRequestService {

    private final WastePickupRequestRepository pickupRequestRepository;

    public PickupRequestService(WastePickupRequestRepository pickupRequestRepository) {
        this.pickupRequestRepository = pickupRequestRepository;
    }

    public Optional<WastePickupRequest> findById(Long id) {
        return pickupRequestRepository.findById(id);
    }

    public List<WastePickupRequest> findAll() {
        return pickupRequestRepository.findAll();
    }

    public List<WastePickupRequest> findByCitizen(User citizen) {
        return pickupRequestRepository.findByCitizenOrderByRequestedDateDesc(citizen);
    }

    public List<WastePickupRequest> findByAssignedStaff(User staff) {
        return pickupRequestRepository.findByAssignedStaffOrderByScheduledDateAsc(staff);
    }

    public List<WastePickupRequest> findByStatus(RequestStatus status) {
        return pickupRequestRepository.findByStatus(status);
    }

    @Transactional
    public WastePickupRequest createRequest(WastePickupRequest request) {
        request.setRequestedDate(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        return pickupRequestRepository.save(request);
    }

    @Transactional
    public WastePickupRequest assignStaff(Long requestId, User staff, LocalDate scheduledDate) {
        WastePickupRequest request = pickupRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
        request.setAssignedStaff(staff);
        request.setScheduledDate(scheduledDate);
        request.setStatus(RequestStatus.ASSIGNED);
        return pickupRequestRepository.save(request);
    }

    @Transactional
    public WastePickupRequest updateStatus(Long requestId, RequestStatus status) {
        WastePickupRequest request = pickupRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
        request.setStatus(status);
        return pickupRequestRepository.save(request);
    }

    @Transactional
    public WastePickupRequest completeRequest(Long requestId, String proofNotes, String proofImage) {
        WastePickupRequest request = pickupRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
        request.setCollectionProofNotes(proofNotes);
        if (proofImage != null && !proofImage.isEmpty()) {
            request.setCollectionProofImage(proofImage);
        }
        request.setStatus(RequestStatus.COMPLETED);
        return pickupRequestRepository.save(request);
    }

    @Transactional
    public WastePickupRequest save(WastePickupRequest request) {
        return pickupRequestRepository.save(request);
    }
}
