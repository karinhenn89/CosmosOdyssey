package com.example.CosmosOdyssey.Service;



import com.example.CosmosOdyssey.Model.Leg;
import com.example.CosmosOdyssey.Model.Providers;
import com.example.CosmosOdyssey.Model.RouteInfo;
import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Repository.LegRepository;
import com.example.CosmosOdyssey.Repository.ProvidersRepository;
import com.example.CosmosOdyssey.Repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ProvidersRepository providersRepository;

    private final String apiURL = "https://cosmosodyssey.azurewebsites.net/api/v1.0/TravelPrices";

    public List<RouteInfo> fetchRoutesFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        TravelPricesResponse response = restTemplate.getForObject(apiURL, TravelPricesResponse.class);

        if (response != null && response.getLegs() != null) {
            for (Leg leg : response.getLegs()) {
                RouteInfo routeInfo = leg.getRouteInfo();
                routeRepository.save(routeInfo);

                // Save Leg with its associated RouteInfo
                leg.setRouteInfo(routeInfo);
                legRepository.save(leg);

                // Save Providers for the current Leg
                if (leg.getProviders() != null) {
                    for (Providers provider : leg.getProviders()) {
                        provider.setLeg(leg);
                        providersRepository.save(provider);
                    }
                }
            }
        }
        return routeRepository.findAll();
    }
}
