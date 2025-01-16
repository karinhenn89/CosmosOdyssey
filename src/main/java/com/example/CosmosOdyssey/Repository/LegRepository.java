package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.Leg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegRepository extends JpaRepository<Leg, String> {

}
