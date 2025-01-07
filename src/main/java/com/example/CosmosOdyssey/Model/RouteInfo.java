package com.example.CosmosOdyssey.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class RouteInfo {
    @Id
    @Column(name = "route_info_id")
    private String Id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name= "id", column = @Column(name = "from_id")),
            @AttributeOverride(name= "name", column = @Column(name = "from_name"))
    })
    private Location from;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name= "id", column = @Column(name = "to_id")),
            @AttributeOverride(name= "name", column = @Column(name = "to_name"))
    })
    private Location to;

    private long distance;

    @OneToMany(mappedBy = "routeInfo")
    private List<Leg> legs;

}
