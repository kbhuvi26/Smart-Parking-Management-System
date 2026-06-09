package com.smartparking.SmartParkingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartparking.SmartParkingSystem.model.Vehicle;
import com.smartparking.SmartParkingSystem.service.VehicleService;
@Controller



   
public class VehicleController {
        
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/vehicle-entry")
public String addVehicle(

        @RequestParam("vehicle_number")
        String vehicleNumber,

        @RequestParam("vehicle_type")
        String vehicleType,

        Model model) {
                System.out.println("ENTRY HIT");
System.out.println(vehicleNumber);
System.out.println(vehicleType);

    Vehicle vehicle =
            vehicleService.addVehicle(
                    vehicleNumber,
                    vehicleType
            );

    /*if(vehicle==null){

        model.addAttribute(
                "message",
                "No slots available. Added to Waiting Queue"
        );

        return "waiting";
    }*/
   if(vehicle==null){

    model.addAttribute(
            "vehicleNumber",
            vehicleNumber
    );

    model.addAttribute(
            "vehicleType",
            vehicleType
    );

    return "waiting-confirmation";
}

    model.addAttribute(
            "vehicle",
            vehicle
    );

    return "success";
}
@PostMapping("/join-queue")
public String joinQueue(

        @RequestParam("vehicle_number")
        String vehicleNumber,

        @RequestParam("vehicle_type")
        String vehicleType,

        Model model){

    vehicleService.addToWaitingQueue(
            vehicleNumber,
            vehicleType
    );

    model.addAttribute(
            "message",
            "Vehicle Added To Waiting Queue"
    );

    return "waiting";
}

@GetMapping("/waiting-queue")
public String waitingQueue(Model model){

    model.addAttribute(
            "queueList",
            vehicleService.getAllWaitingVehicles()
    );

    return "waiting-queue";
}

@PostMapping("/allocate-slot")
public String allocateSlot(

        @RequestParam("queue_id")
        Integer queueId,

        Model model){

    Vehicle vehicle =
            vehicleService
            .allocateWaitingVehicle(
                    queueId
            );

    if(vehicle == null){

        model.addAttribute(
                "message",
                "No Free Slot Available"
        );

        return "waiting";
    }

    model.addAttribute(
            "vehicle",
            vehicle
    );

    return "success";
}
@PostMapping("/reject-slot")
public String rejectSlot(

        @RequestParam("queue_id")
        Integer queueId){

    vehicleService.rejectWaitingVehicle(
            queueId
    );

    return "redirect:/waiting-queue";
}
}