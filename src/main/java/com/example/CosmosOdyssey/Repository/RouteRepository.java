package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.RouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<RouteInfo, Long> {
    List<RouteInfo> findByFromNameAndToName(String fromName, String toName);

    @Query("SELECT DISTINCT r.from.name FROM RouteInfo r")
    List<String> findDistinctFromNames();

    @Query("SELECT DISTINCT r.to.name FROM RouteInfo r WHERE r.from.name = :fromName")
    List<String> findDistinctToNamesByFromName(@Param("fromName") String fromName);
}
