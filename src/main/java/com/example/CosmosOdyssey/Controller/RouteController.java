package com.example.CosmosOdyssey.Controller;


import com.example.CosmosOdyssey.Model.LocationDto;
import com.example.CosmosOdyssey.Model.ProviderDto;
import com.example.CosmosOdyssey.Model.RouteInfo;

import com.example.CosmosOdyssey.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    public List<RouteInfo> getAllRoutes() {
        return routeService.fetchRoutesFromApi();
    }

    @GetMapping("/is-valid")
    public String isValidDate () {
        return routeService.isValid();
    }

    @GetMapping("/getproviders")
    public List<ProviderDto> getProvidersBasedOnOriginAndDestination(
            @RequestParam String fromName,
            @RequestParam String toName) {

        return routeService.getProvidersBasedOnOriginAndDestination(fromName, toName);
    }

    @GetMapping("/routes/locations")
    public Map<String, List<String>> getAllLocations() {
        return routeService.getAllFromLocations();
    }

    @GetMapping("/routes/locations/{fromName}")
    public Map<String, List<String>> getLocationOptions(@PathVariable String fromName) {
        return routeService.getLocationOptions(fromName);
    }

}
