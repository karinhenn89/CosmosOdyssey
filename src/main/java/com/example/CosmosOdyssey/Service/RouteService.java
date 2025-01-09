package com.example.CosmosOdyssey.Service;



import com.example.CosmosOdyssey.Model.Leg;
import com.example.CosmosOdyssey.Model.Providers;
import com.example.CosmosOdyssey.Model.RouteInfo;
import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Repository.LegRepository;
import com.example.CosmosOdyssey.Repository.ProvidersRepository;
import com.example.CosmosOdyssey.Repository.RouteRepository;
import com.example.CosmosOdyssey.Repository.TravelPricesResponseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class RouteService {

    private String validUntil;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ProvidersRepository providersRepository;

    @Autowired
    private TravelPricesResponseRepository travelPricesResponseRepository;

    private final String apiURL = "https://cosmosodyssey.azurewebsites.net/api/v1.0/TravelPrices";


    @PostConstruct                          //runs data method right on a startup
    public void fetchRoutesFromApiOnStartup() {
        fetchRoutesFromApi();
    }


    public List<RouteInfo> fetchRoutesFromApi() {               //Takes all data from url and writes into H2
        RestTemplate restTemplate = new RestTemplate();
        TravelPricesResponse response = restTemplate.getForObject(apiURL, TravelPricesResponse.class);

        if (response != null && response.getLegs() != null) {

            // Accessing validUntil date
            this.validUntil = response.getValidUntil();
            System.out.println("Price list is valid until: " + this.validUntil);

            for (Leg leg : response.getLegs()) {
                RouteInfo routeInfo = leg.getRouteInfo();


                // Save Leg with its associated RouteInfo
                leg.setRouteInfo(routeInfo);
                routeRepository.save(routeInfo);

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

    public String isValid() {

            try {
                ZonedDateTime priceListDate = ZonedDateTime.parse(this.validUntil);
                System.out.println("Parsed date: " + priceListDate);

            // Compare with current date and time
            ZonedDateTime currentDateTime = ZonedDateTime.now();
            System.out.println("Current date: " + currentDateTime);

            if (priceListDate.isBefore(currentDateTime)) {
                return "Pricelist is available.";
            } else {
                return "Pricelist is not available.";
            }

        } catch (DateTimeParseException e) {
            return "Invalid date format.";
        }

    }

}
