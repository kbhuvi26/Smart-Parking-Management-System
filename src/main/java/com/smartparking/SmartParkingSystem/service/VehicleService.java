package com.smartparking.SmartParkingSystem.service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.smartparking.SmartParkingSystem.model.Slot;
import com.smartparking.SmartParkingSystem.model.Vehicle;
import com.smartparking.SmartParkingSystem.model.WaitingQueue;
import com.smartparking.SmartParkingSystem.repository.SlotRepository;
import com.smartparking.SmartParkingSystem.repository.VehicleRepository;
import com.smartparking.SmartParkingSystem.repository.WaitingQueueRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    // VEHICLE ENTRY

    public Vehicle addVehicle(String vehicleNumber,
                              String vehicleType) {


        Vehicle vehicle = new Vehicle();
         Vehicle existing =
            vehicleRepository
            .findByVehicleNumberAndExitTimeIsNull(
                    vehicleNumber
            );

    if(existing != null){
        return null;
    }

        vehicle.setVehicleNumber(vehicleNumber);
        vehicle.setVehicleType(vehicleType);
        vehicle.setEntryTime(LocalDateTime.now());

        Slot slot =
                slotRepository
                .findFirstBySlotTypeAndSlotStatusOrderByDistanceAsc(
                        vehicleType,
                        "FREE"
                );

        if(slot != null){

            vehicle.setSlotId(
                    slot.getSlotId()
            );

            slot.setSlotStatus(
                    "OCCUPIED"
            );

            slotRepository.save(
                    slot
            );

            return vehicleRepository.save(
                    vehicle
            );
        }

        // No slot available
        return null;
    }

    // ADD TO WAITING QUEUE

    public void addToWaitingQueue(
            String vehicleNumber,
            String vehicleType){

        WaitingQueue waitingVehicle =
                new WaitingQueue();

        waitingVehicle.setVehicleNumber(
                vehicleNumber
        );

        waitingVehicle.setVehicleType(
                vehicleType
        );

        waitingVehicle.setJoinTime(
                LocalDateTime.now()
        );

        waitingVehicle.setEstimatedTime(
                10
        );

        waitingQueueRepository.save(
                waitingVehicle
        );
    }

    // VEHICLE EXIT

    public Vehicle exitVehicle(
        String vehicleNumber,
        String paymentMethod,
        String email){

        Vehicle vehicle =
                vehicleRepository
                .findByVehicleNumberAndExitTimeIsNull(
                        vehicleNumber
                );

        if(vehicle == null){
            return null;
        }

        vehicle.setExitTime(
                LocalDateTime.now()
        );

        vehicle.setFee(
                20.0
        );
        vehicle.setPaymentMethod(
        paymentMethod
);

        String freedSlotId =
                vehicle.getSlotId();

        vehicleRepository.save(
                vehicle
        );

        Slot freedSlot =
                slotRepository
                .findById(freedSlotId)
                .orElse(null);

        if(freedSlot != null){

            freedSlot.setSlotStatus(
                    "FREE"
            );

            slotRepository.save(
                    freedSlot
            );
        }
        DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern(
                "dd-MM-yyyy hh:mm a"
        );
        try {

    EmailService.sendEmailReceipt(


            email,

            vehicle.getVehicleNumber(),

            vehicle.getFee(),

            vehicle.getEntryTime().format(formatter),

            vehicle.getExitTime().format(formatter)

    );

}
catch(Exception e){

    e.printStackTrace();

}
        return vehicle;
    }
    public List<WaitingQueue> getAllWaitingVehicles(){
    return waitingQueueRepository.findAll();
}
public void rejectWaitingVehicle(
        Integer queueId){

    waitingQueueRepository.deleteById(
            queueId
    );
}
public long getTotalSlots(){
    return slotRepository.count();
}

public long getAvailableSlots(){
    return slotRepository.countBySlotStatus("FREE");
}

public long getOccupiedSlots(){
    return slotRepository.countBySlotStatus("OCCUPIED");
}

public long getQueueCount(){
    return waitingQueueRepository.count();
}
public Double getTotalRevenue(){

    return vehicleRepository.getTotalRevenue();
}
public List<Slot> getAllSlots(){

    return slotRepository.findAll();
}
public List<Vehicle> getAllPayments(){

    return vehicleRepository
             .findByFeeIsNotNullOrderByExitTimeDesc();
}


public List<Vehicle> getRecentActivities(){

    return vehicleRepository
            .findAllByOrderByEntryTimeDesc(
                    PageRequest.of(0,5)
            );
}
    // MANUAL SLOT ALLOCATION FROM WAITING QUEUE

    public Vehicle allocateWaitingVehicle(
            Integer queueId){

        WaitingQueue waitingVehicle =
                waitingQueueRepository
                .findById(queueId)
                .orElse(null);

        if(waitingVehicle == null){
            return null;
        }

        Slot slot =
                slotRepository
                .findFirstBySlotTypeAndSlotStatusOrderByDistanceAsc(
                        waitingVehicle.getVehicleType(),
                        "FREE"
                );

        if(slot == null){
            return null;
        }

        Vehicle vehicle =
                new Vehicle();

        vehicle.setVehicleNumber(
                waitingVehicle.getVehicleNumber()
        );

        vehicle.setVehicleType(
                waitingVehicle.getVehicleType()
        );

        vehicle.setSlotId(
                slot.getSlotId()
        );

        vehicle.setEntryTime(
                LocalDateTime.now()
        );

        slot.setSlotStatus(
                "OCCUPIED"
        );

        slotRepository.save(
                slot
        );

        vehicleRepository.save(
                vehicle
        );

        waitingQueueRepository.delete(
                waitingVehicle
        );

        return vehicle;
    }
}