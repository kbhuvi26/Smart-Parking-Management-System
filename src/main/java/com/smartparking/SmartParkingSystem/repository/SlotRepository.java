package com.smartparking.SmartParkingSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.SmartParkingSystem.model.Slot;

public interface SlotRepository
        extends JpaRepository<Slot,String> {

    Slot findFirstBySlotTypeAndSlotStatusOrderByDistanceAsc(
            String slotType,
            String slotStatus
    );
    long countBySlotStatus(String slotStatus);

}