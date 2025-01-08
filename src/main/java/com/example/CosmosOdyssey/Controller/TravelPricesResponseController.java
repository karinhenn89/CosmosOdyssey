package com.example.CosmosOdyssey.Controller;


import com.example.CosmosOdyssey.Service.TravelPricesResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "*")
public class TravelPricesResponseController {

    @Autowired
    private TravelPricesResponseService travelPricesResponseService;


    @GetMapping("/isvalid")
    public void isValid() {
        travelPricesResponseService.isValidData();
    }

}
