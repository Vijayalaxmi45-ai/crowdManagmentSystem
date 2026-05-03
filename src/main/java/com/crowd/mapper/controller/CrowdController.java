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

    @GetMapping("/live-tracking")
    public String liveTrackingPage(Model model) {
        model.addAttribute("locations", crowdService.getAllLocations());
        return "live-tracking";
    }

    @GetMapping("/qr-entry")
    public String qrEntryPage() {
        return "qr-entry";
    }

    @GetMapping("/api/locations")
    @ResponseBody
    public List<CrowdLocation> getLocationsApi() {
        return crowdService.getAllLocations();
    }
}
