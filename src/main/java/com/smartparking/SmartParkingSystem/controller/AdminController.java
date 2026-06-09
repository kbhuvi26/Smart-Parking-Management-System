package com.smartparking.SmartParkingSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("/admin-login")
    public String adminLoginPage() {

        return "admin-login";
    }

    @PostMapping("/admin-login")
    public String adminLogin(

            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        if(username.equals("admin")
                && password.equals("admin123")) {

    session.setAttribute(
            "admin",
            true
    );
    session.setAttribute(
            "username",
            username
    );

            return "redirect:/dashboard";
        }

        return "admin-login";
    }
    @GetMapping("/logout")
public String logout(HttpSession session){

    session.invalidate();

    return "redirect:/admin-login";
}
}