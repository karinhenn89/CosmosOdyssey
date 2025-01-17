package com.example.CosmosOdyssey.Controller;


import com.example.CosmosOdyssey.Model.ProviderDto;
import com.example.CosmosOdyssey.Model.RouteInfo;

import com.example.CosmosOdyssey.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    public List<RouteInfo> getAllRoutes() {
        return routeService.fetchRoutesFromApi();
    }

    @GetMapping("/getproviders")
    public List<ProviderDto> getProvidersBasedOnOriginAndDestination(
            @RequestParam String fromName,
            @RequestParam String toName) {

        return routeService.getProvidersBasedOnOriginAndDestination(fromName, toName);
    }

    @GetMapping("/locations")
    public Map<String, List<String>> getAllLocations() {
        return routeService.getAllFromLocations();
    }

    @GetMapping("/locations/{fromName}")
    public Map<String, List<String>> getLocationFromOptionsTo(@PathVariable String fromName) {
        return routeService.getLocationFromOptionsTo(fromName);
    }

}
