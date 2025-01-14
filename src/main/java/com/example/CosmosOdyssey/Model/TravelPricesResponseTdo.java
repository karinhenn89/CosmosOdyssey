package com.example.CosmosOdyssey.Model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelPricesResponseTdo {

    private String id;
    private String validUntil;

    private String legId;
    private String routeInfoId;
    private long distance;

    private String fromId;
    private String fromName;
    private String toId;
    private String toName;

    private String providerId;
    private String companyId;
    private String companyName;
    private String flightStart;
    private String flightEnd;
    private double price;


    public TravelPricesResponseTdo(TravelPricesResponse travelPricesResponse,Leg leg, RouteInfo routeInfo, Location location, Company company, Providers provider) {
        this.id = travelPricesResponse.getId();
        this.validUntil = travelPricesResponse.getValidUntil();
        this.legId = leg.getId();
        this.routeInfoId = routeInfo.getId();
        this.fromId = location.getId();
        this.fromName = location.getName();
        this.toId =location.getId();
        this.toName = location.getName();
        this.providerId = provider.getId();
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.flightStart = provider.getFlightStart();
        this.flightEnd = provider.getFlightEnd();
        this.price = provider.getPrice();





    }





}
