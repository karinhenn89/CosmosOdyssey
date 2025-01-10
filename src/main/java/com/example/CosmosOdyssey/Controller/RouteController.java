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
    public List<ProviderDto> getProvidersBasedOnOriginAndDestination(@RequestBody LocationDto locationRequest){
        return routeService.getProvidersBasedOnOriginAndDestination(locationRequest.getFromName(), locationRequest.getToName());
    }

    @GetMapping("/routes/locations")
    public Map<String, List<String>> getLocationOptions() {
        return routeService.getLocationOptions();
    }

}
