package com.example.CosmosOdyssey.Model;

import lombok.Data;

import java.util.List;

            //Wrapper class
@Data
public class TravelPricesResponse {
    private String id;
    private String validUntil;
    private List<Leg> legs;
}
