package com.example.CosmosOdyssey.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Leg {
    @Id
    private String id;


    @ManyToOne  // Add this mapping
    @JoinColumn(name = "route_info_id")
    private RouteInfo routeInfo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy ="leg" )
    private List<Providers> providers;
}
