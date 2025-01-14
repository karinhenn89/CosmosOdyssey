package com.example.CosmosOdyssey.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String routes;
    private double totalQuotedPrice;
    private long totalQuotedTravelTime;
    private String transportationCompanyNames;
}
