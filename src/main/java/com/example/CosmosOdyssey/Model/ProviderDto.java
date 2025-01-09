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


    public ProviderDto(Company company, String flightStart, String flightEnd, Double price) {
        this.companyName = company.getName();
        this.flightStart = flightStart;
        this.flightEnd = flightEnd;
        this.price = price;
    }
}
