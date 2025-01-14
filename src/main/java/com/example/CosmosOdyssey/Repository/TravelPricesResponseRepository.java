package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPricesResponseRepository extends JpaRepository<TravelPricesResponse, String> {
    @Query("SELECT t FROM TravelPricesResponse t WHERE t.deleted = false ORDER BY t.validUntil DESC")
    List<TravelPricesResponse> findAllActivePricelists(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE TravelPricesResponse t SET t.deleted = true WHERE t.id = :id")
    void softDeleteById(@Param("id") String id);

    @Query("SELECT t FROM TravelPricesResponse t WHERE t.deleted = false AND t.id = :id")
    Optional<TravelPricesResponse> findActiveById(@Param("id") String id);

    @Query("SELECT COUNT(t) FROM TravelPricesResponse t WHERE t.deleted = false")
    long countActivePricelists();

    TravelPricesResponse findTopByOrderByValidUntilDesc();

}
