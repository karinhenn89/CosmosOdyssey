package com.example.CosmosOdyssey.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

            //Wrapper class
@Data
@Entity
@Getter
@Setter
public class TravelPricesResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "validUntil")
    private String validUntil;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "leg_id")
    private List<Leg> legs;
}
