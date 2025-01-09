package com.example.CosmosOdyssey.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Company {
    @Column(name = "company_id")
    private String id;
    private String name;
}
