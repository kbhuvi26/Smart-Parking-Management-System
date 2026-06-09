package com.smartparking.SmartParkingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smartparking.SmartParkingSystem.repository.SlotRepository;
import com.smartparking.SmartParkingSystem.service.VehicleService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private SlotRepository slotRepository;

    @GetMapping("/dashboard")
    public String dashboard( HttpSession session,Model model){
        if(session.getAttribute("admin")
        == null){

    return "redirect:/admin-login";
}

        model.addAttribute(
                "totalSlots",
                vehicleService.getTotalSlots()
        );

        model.addAttribute(
                "availableSlots",
                vehicleService.getAvailableSlots()
        );

        model.addAttribute(
                "occupiedSlots",
                vehicleService.getOccupiedSlots()
        );

        model.addAttribute(
                "queueCount",
                vehicleService.getQueueCount()
        );

        model.addAttribute(
        "totalRevenue",
        vehicleService.getTotalRevenue()
       );

       model.addAttribute(
        "activities",
        vehicleService.getRecentActivities()
        );
        return "dashboard";
    }
    @GetMapping("/vehicle-entry")
public String vehicleEntry(HttpSession session) {
        if(session.getAttribute("admin")
            == null){

        return "redirect:/admin-login";
    }

    return "vehicle-entry";
}
@GetMapping("/vehicle-exit")
public String vehicleExit(HttpSession session) {
        if(session.getAttribute("admin")
            == null){

        return "redirect:/admin-login";
    }

        
    return "vehicle-exit";
}

@GetMapping("/payments")
public String payments(
        Model model,HttpSession session){
                if(session.getAttribute("admin")
            == null){

        return "redirect:/admin-login";
    }


    model.addAttribute(
            "payments",
            vehicleService.getAllPayments()
    );

    return "payments";
}
@GetMapping("/slot-visualization")
public String slotVisualization(
        Model model,HttpSession session){
                if(session.getAttribute("admin")
            == null){

        return "redirect:/admin-login";
    }


    model.addAttribute(
            "slots",
            slotRepository.findAll()
    );

    return "slot-visualization";
}


}