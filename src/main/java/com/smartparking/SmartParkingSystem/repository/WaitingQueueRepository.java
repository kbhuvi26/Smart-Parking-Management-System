package com.smartparking.SmartParkingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartparking.SmartParkingSystem.model.WaitingQueue;

public interface WaitingQueueRepository
        extends JpaRepository<WaitingQueue,Integer> {

    WaitingQueue findFirstByVehicleTypeOrderByJoinTimeAsc(
            String vehicleType
    );
}