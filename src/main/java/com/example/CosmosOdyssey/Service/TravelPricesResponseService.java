package com.example.CosmosOdyssey.Service;


import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Repository.TravelPricesResponseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelPricesResponseService {

    @Autowired
    TravelPricesResponseRepository travelPricesResponseRepository;

    TravelPricesResponse travelPricesResponse;

    @Transactional
    public void saveTravelPricesResponse() {
        travelPricesResponseRepository.save(travelPricesResponse);
    }

}


