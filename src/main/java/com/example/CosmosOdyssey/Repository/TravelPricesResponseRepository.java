package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TravelPricesResponseRepository extends JpaRepository<TravelPricesResponse, Long> {
}
