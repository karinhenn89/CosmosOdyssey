package com.example.CosmosOdyssey.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;


@Embeddable
@Data
public class Location {
    private String id;
    private String name;
}
