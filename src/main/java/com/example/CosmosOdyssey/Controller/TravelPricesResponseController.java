package com.example.CosmosOdyssey.Controller;


import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Service.TravelPricesResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/pricelist")
@RestController
@CrossOrigin(origins = "*")
public class TravelPricesResponseController {

    @Autowired
    private TravelPricesResponseService travelPricesResponseService;

    @GetMapping("/latest")
    public ResponseEntity<TravelPricesResponse> getLatestValidPricelist() {
        try {
            TravelPricesResponse latestPricelist = travelPricesResponseService.getLatestValidPricelist();
            return ResponseEntity.ok(latestPricelist);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
