package com.example.CosmosOdyssey.Controller;


import com.example.CosmosOdyssey.Model.RouteInfo;
import com.example.CosmosOdyssey.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    public List<RouteInfo> getAllRoutes() {
        return routeService.fetchRoutesFromApi();
    }
}
