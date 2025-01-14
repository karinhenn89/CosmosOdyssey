package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepositroy extends JpaRepository<Reservation, Long> {
    // Query to find reservations based on validUntil field
    List<Reservation> findByValidUntil(String validUntil);

}
