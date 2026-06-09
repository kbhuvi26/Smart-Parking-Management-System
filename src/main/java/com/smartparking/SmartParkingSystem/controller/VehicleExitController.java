package com.smartparking.SmartParkingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartparking.SmartParkingSystem.model.Vehicle;
import com.smartparking.SmartParkingSystem.service.VehicleService;


@Controller

public class VehicleExitController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/vehicle-exit")
public String exitVehicle(

        @RequestParam("vehicle_number")
        String vehicleNumber,

        @RequestParam("email")
        String email,

        @RequestParam("paymentMethod")
        String paymentMethod,

        Model model) {

    Vehicle vehicle =
            vehicleService
            .exitVehicle(
                    vehicleNumber,
                    paymentMethod,
                    email
            );

    if(vehicle == null){

        model.addAttribute(
                "message",
                "Vehicle Not Found"
        );

        return "waiting";
    }

    model.addAttribute(
            "vehicle",
            vehicle
    );

    return "exit-success";
}
}