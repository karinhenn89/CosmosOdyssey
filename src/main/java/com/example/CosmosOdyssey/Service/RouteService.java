package com.example.CosmosOdyssey.Service;

import com.example.CosmosOdyssey.Model.*;
import com.example.CosmosOdyssey.Repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.OffsetDateTime;
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
    @Autowired
    private ReservationService reservationService;

    @PostConstruct                          //runs data method right on a startup
    public void fetchRoutesFromApiOnStartup() {
        fetchRoutesFromApi();
    }

    @Scheduled(fixedRate = 60000) // This checks every minute (pricelists changeing every 15.29.xx, maybe to change this scheduling date longer?)
    public void checkAndUpdatePriceList() {

        System.out.println("Scheduled task executed at: " + Instant.now());

        TravelPricesResponse travelPricesResponse = travelPricesResponseRepository.findTopByOrderByValidUntilDesc(); // Fetch the last price list data

        if (travelPricesResponse != null && !travelPricesResponse.isDeleted()) {
            try {

                Instant validUntilInstant = OffsetDateTime.parse(travelPricesResponse.getValidUntil()).toInstant();
                Instant now = Instant.now();

                // Debug logs
                System.out.println("ValidUntil: " + validUntilInstant);
                System.out.println("Now: " + now);

                // Check if the validUntil time has passed
                if (now.isAfter(validUntilInstant)) {
                    System.out.println("ValidUntil expired. Refreshing data...");
                    refreshPriceListData();
                } else {
                    System.out.println("ValidUntil not expired. No refresh needed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No valid TravelPricesResponse found or it is marked as deleted.");
        }
    }

    public void softDeleteExpiredPriceLists() {
        long activeCount = travelPricesResponseRepository.countActivePricelists();
        if (activeCount > 15) {
            int deleteCount = (int) (activeCount - 15);
            Pageable pageable = PageRequest.of(0, deleteCount);
            List<TravelPricesResponse> oldestPricelists = travelPricesResponseRepository.findAllActivePricelists(pageable);

            for (TravelPricesResponse oldest : oldestPricelists) {
                travelPricesResponseRepository.softDeleteById(oldest.getId());
            }
        }
    }

    // Method to refresh price list data by calling the URL API
    private void refreshPriceListData() {
        fetchRoutesFromApi();
        softDeleteExpiredPriceLists();
        reservationService.deleteExpiredReservations();
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

        String validUntil = latestPriceList.getValidUntil();

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
                                routeInfo.getDistance(),
                                validUntil

                        ))
                )
                .collect(Collectors.toList());
    }

    //Get all locations FROM planets
    public Map<String, List<String>> getAllFromLocations() {
        List<String> fromOptions = routeRepository.findDistinctFromNames();
        Map<String, List<String>> allLocationOptions = new HashMap<>();
        allLocationOptions.put("fromOptions", fromOptions);
        return allLocationOptions;
    }

    //fetch toName according to From
    public Map<String, List<String>> getLocationFromOptionsTo(String fromName) {
        List<String> fromOptions = routeRepository.findDistinctFromNames();
        List<String> toOptions = routeRepository.findDistinctToNamesByFromName(fromName);

        // Create a map to hold the results
        Map<String, List<String>> locationOptions = new HashMap<>();
        locationOptions.put("fromOptions", fromOptions);
        locationOptions.put("toOptions", toOptions);

        return locationOptions;
    }
}
