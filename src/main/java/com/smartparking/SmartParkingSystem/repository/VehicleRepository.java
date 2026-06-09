package com.smartparking.SmartParkingSystem.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartparking.SmartParkingSystem.model.Vehicle;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle,Integer> {

    Vehicle findByVehicleNumberAndExitTimeIsNull(
            String vehicleNumber
    );

    @Query("SELECT COALESCE(SUM(v.fee),0) FROM Vehicle v")
    Double getTotalRevenue();

    List<Vehicle> findByFeeIsNotNullOrderByExitTimeDesc();
    List<Vehicle> findAllByOrderByEntryTimeDesc(Pageable pageable);
}