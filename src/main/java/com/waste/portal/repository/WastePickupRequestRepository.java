package com.waste.portal.repository;

import com.waste.portal.model.WastePickupRequest;
import com.waste.portal.model.User;
import com.waste.portal.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WastePickupRequestRepository extends JpaRepository<WastePickupRequest, Long> {
    List<WastePickupRequest> findByCitizenOrderByRequestedDateDesc(User citizen);
    List<WastePickupRequest> findByAssignedStaffOrderByScheduledDateAsc(User staff);
    List<WastePickupRequest> findByStatus(RequestStatus status);
}
