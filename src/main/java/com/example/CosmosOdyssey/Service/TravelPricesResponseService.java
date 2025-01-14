package com.example.CosmosOdyssey.Service;


import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Repository.TravelPricesResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TravelPricesResponseService {

    @Autowired
    private TravelPricesResponseRepository travelPricesResponseRepository;

    public TravelPricesResponse getLatestValidPricelist() {
        // Fetch all active pricelists using the existing repository method
        Pageable pageable = PageRequest.of(0, 15, Sort.by("validUntil").descending());
        List<TravelPricesResponse> pricelists = travelPricesResponseRepository.findAllActivePricelists(pageable);

        // Filter for the first valid pricelist
        return pricelists.stream()
                .filter(pricelist -> {
                    try {
                        // Parse the validUntil timestamp as ZonedDateTime to account for time zone (UTC)
                        ZonedDateTime validUntil = ZonedDateTime.parse(pricelist.getValidUntil(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                        // Compare with the current time in UTC
                        return validUntil.isAfter(ZonedDateTime.now(ZoneOffset.UTC));
                    } catch (DateTimeParseException e) {
                        // Log the error and skip this pricelist
                        System.err.println("Failed to parse validUntil for pricelist ID " + pricelist.getId() + ": " + e.getMessage());
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No valid pricelist available"));
    }

}


