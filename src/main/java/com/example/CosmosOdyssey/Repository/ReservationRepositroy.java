package com.example.CosmosOdyssey.Repository;

import com.example.CosmosOdyssey.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepositroy extends JpaRepository<Reservation, Long> {
}
