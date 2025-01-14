package com.example.CosmosOdyssey.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private String firstName;
    private String lastName;

    @ElementCollection
    private List<String> routes;

    private double totalQuotedPrice;
    private String totalQuotedTravelTime;

    @ElementCollection
    private List<String> companyNames;

    private String validUntil;



}
