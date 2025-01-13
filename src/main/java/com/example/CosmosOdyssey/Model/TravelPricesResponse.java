package com.example.CosmosOdyssey.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

            //Wrapper class
@Data
@Entity
@Table(name= "travel_prices_response")
@Getter
@Setter
public class TravelPricesResponse {
    @Id
    @Column(name = "price_list_id", nullable = false)
    private String id;

    @Column(name = "valid_Until")
    private String validUntil;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "travelPricesResponse")
    private List<Leg> legs;
}
