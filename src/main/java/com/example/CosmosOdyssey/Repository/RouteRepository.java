package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.RouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<RouteInfo, Long> {
    List<RouteInfo> findByFromNameAndToName(String fromName, String toName);

}
