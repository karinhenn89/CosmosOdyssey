package com.example.CosmosOdyssey.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Providers {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "leg_id")
    private Leg leg;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "company_id"))
    })
    private Company company;

    private double price;
    private String flightStart;
    private String flightEnd;

}
