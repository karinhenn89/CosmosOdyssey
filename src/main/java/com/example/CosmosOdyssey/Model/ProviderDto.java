package com.example.CosmosOdyssey.Model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderDto {
    private String companyName;
    private String flightStart;
    private String flightEnd;
    private Double price;
    private String fromName; // From RouteInfo's 'from' Location
    private String toName;   // From RouteInfo's 'to' Location


    public ProviderDto (String companyName, String flightStart, String flightEnd, Double price, String fromName, String toName) {
        this.companyName = companyName;
        this.flightStart = flightStart;
        this.flightEnd = flightEnd;
        this.price = price;
        this.fromName = fromName;
        this.toName = toName;

    }
    public ProviderDto (Company company, String flightStart, String flightEnd, Double price, Location from, Location to) {
        this.companyName = company.getName();
        this.flightStart = flightStart;
        this.flightEnd = flightEnd;
        this.price = price;
        this.fromName = from.getName(); // Extract from Location
        this.toName = to.getName();     // Extract from Location
    }
}
