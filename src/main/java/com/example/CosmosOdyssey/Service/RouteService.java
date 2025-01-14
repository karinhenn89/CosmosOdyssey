package com.example.CosmosOdyssey.Service;



import com.example.CosmosOdyssey.Model.*;
import com.example.CosmosOdyssey.Repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private String validUntil;
    private String id;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ProvidersRepository providersRepository;

    @Autowired
    private TravelPricesResponseRepository travelPricesResponseRepository;

    @Autowired
    TravelPricesResponseService travelPricesResponseService;

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
            this.id = response.getId();

            System.out.println("Price list is valid until: " + this.validUntil + " id: " + this.id);

            if (this.id == null || this.id.isEmpty()) {
                throw new IllegalStateException("ID from API response is null or empty");
            }

            // Check if pricelist already exists and is soft-deleted
            Optional<TravelPricesResponse> existingPricelist = travelPricesResponseRepository.findActiveById(this.id);


            TravelPricesResponse travelPricesEntity;
            if (existingPricelist.isPresent()) {
                // Update existing pricelist if found
                travelPricesEntity = existingPricelist.get();
                travelPricesEntity.setValidUntil(this.validUntil);
            } else {
                // Save new pricelist
                travelPricesEntity = new TravelPricesResponse();
                travelPricesEntity.setId(this.id);
                travelPricesEntity.setValidUntil(this.validUntil);

                // Check for more than 15 active pricelists and soft-delete the oldest ones
                long activeCount = travelPricesResponseRepository.count();
                if (activeCount >= 15) {
                    Pageable pageable = PageRequest.of(0, (int) (activeCount - 14)); // Keep only 14 active pricelists
                    List<TravelPricesResponse> oldestPricelists = travelPricesResponseRepository.findAllActivePricelists(pageable);
                    for (TravelPricesResponse oldest : oldestPricelists) {
                        travelPricesResponseRepository.softDeleteById(oldest.getId());
                    }
                }
            }

            travelPricesResponseRepository.save(travelPricesEntity);

            for (Leg leg : response.getLegs()) {
                RouteInfo routeInfo = leg.getRouteInfo();

                // Save Leg with its associated RouteInfo
                leg.setRouteInfo(routeInfo);

                leg.setTravelPricesResponse(travelPricesEntity);

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

    public List<ProviderDto> getProvidersBasedOnOriginAndDestination(String fromName, String toName) {
        // Fetch RouteInfo based on fromName and toName
        List<RouteInfo> routeInfos = routeRepository.findByFromNameAndToName(fromName, toName);
        if (routeInfos.isEmpty()) {
            return Collections.emptyList(); // No matching routes
        }

        // Get the latest valid price list
        TravelPricesResponse latestPriceList = travelPricesResponseService.getLatestValidPricelist();

        // Extract Legs from the latest price list
        List<Leg> validLegs = latestPriceList.getLegs();

        // Filter Legs to include only those that match the given RouteInfo
        Set<String> validRouteInfoIds = routeInfos.stream()
                .map(RouteInfo::getId)
                .collect(Collectors.toSet());

        List<Leg> filteredLegs = validLegs.stream()
                .filter(leg -> validRouteInfoIds.contains(leg.getRouteInfo().getId()))
                .collect(Collectors.toList());

        // Extract Leg IDs for filtering Providers
        List<String> validLegIds = filteredLegs.stream()
                .map(Leg::getId)
                .collect(Collectors.toList());

        // Find Providers corresponding to the filtered Legs
        List<Providers> providers = providersRepository.findByLegIdIn(validLegIds);

        // Map Providers to ProviderDto
        return providers.stream()
                .flatMap(provider -> routeInfos.stream()
                        .filter(routeInfo -> routeInfo.getId().equals(provider.getLeg().getRouteInfo().getId()))
                        .map(routeInfo -> new ProviderDto(
                                provider.getCompany(),
                                provider.getFlightStart(),
                                provider.getFlightEnd(),
                                provider.getPrice(),
                                routeInfo.getFrom(),
                                routeInfo.getTo(),
                                routeInfo.getDistance()
                        ))
                )
                .collect(Collectors.toList());
    }

    public Map<String, List<String>> getAllFromLocations() {
        List<String> fromOptions = routeRepository.findDistinctFromNames();
        Map<String, List<String>> allLocationOptions = new HashMap<>();
        allLocationOptions.put("fromOptions", fromOptions);
        return allLocationOptions;
    }

    public Map<String, List<String>> getLocationOptions(String fromName) {
        // Fetch distinct fromName and toName
        List<String> fromOptions = routeRepository.findDistinctFromNames();
        List<String> toOptions = routeRepository.findDistinctToNamesByFromName(fromName);

        // Create a map to hold the results
        Map<String, List<String>> locationOptions = new HashMap<>();
        locationOptions.put("fromOptions", fromOptions);
        locationOptions.put("toOptions", toOptions);

        return locationOptions;
    }
}
