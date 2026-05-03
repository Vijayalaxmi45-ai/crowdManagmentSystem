package com.crowd.mapper.controller;

import com.crowd.mapper.model.CrowdLocation;
import com.crowd.mapper.service.CrowdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CrowdController {

    @Autowired
    private CrowdService crowdService;

    @Autowired
    private com.crowd.mapper.service.ProblemService problemService;

    @GetMapping("/live-tracking")
    public String liveTrackingPage(Model model, jakarta.servlet.http.HttpServletRequest request) {
        // Pass user info
        var principal = request.getUserPrincipal();
        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isStaff", request.isUserInRole("STAFF"));
        }
        model.addAttribute("isLoggedIn", principal != null);
        
        model.addAttribute("locations", crowdService.getAllLocations());
        model.addAttribute("problems", problemService.getAllProblemsSorted());
        return "live-tracking";
    }

    @GetMapping("/qr-entry")
    public String qrEntryPage(Model model, jakarta.servlet.http.HttpServletRequest request) {
        var principal = request.getUserPrincipal();
        if (principal != null) {
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isStaff", request.isUserInRole("STAFF"));
        }
        return "qr-entry";
    }

    @GetMapping("/api/locations")
    @ResponseBody
    public List<CrowdLocation> getLocationsApi() {
        return crowdService.getAllLocations();
    }
}
