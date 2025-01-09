package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.Leg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LegRepository extends JpaRepository<Leg, String> {
    List<Leg> findByRouteInfoIdIn(List<String> routeInfoIds);
}
