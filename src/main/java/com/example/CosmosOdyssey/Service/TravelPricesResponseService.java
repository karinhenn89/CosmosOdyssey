package com.example.CosmosOdyssey.Service;


import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Repository.TravelPricesResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelPricesResponseService {

    @Autowired
    private TravelPricesResponseRepository travelPricesResponseRepository;
    private TravelPricesResponse travelPricesResponse;


    public void isValidData() {
        System.out.println(travelPricesResponse.getValidUntil());
    }
}


