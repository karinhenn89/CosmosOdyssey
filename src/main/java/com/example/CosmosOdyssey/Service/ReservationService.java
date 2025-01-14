package com.example.CosmosOdyssey.Service;


import com.example.CosmosOdyssey.Model.Reservation;
import com.example.CosmosOdyssey.Model.TravelPricesResponse;
import com.example.CosmosOdyssey.Repository.ReservationRepositroy;
import com.example.CosmosOdyssey.Repository.TravelPricesResponseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ReservationService {

    @Autowired
    private ReservationRepositroy reservationRepository;

    @Autowired
    private TravelPricesResponseRepository travelPricesResponseRepository;

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteExpiredReservations() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Get all expired TravelPricesResponse entries with deleted = true
        List<TravelPricesResponse> expiredResponses = travelPricesResponseRepository
                .findByDeletedTrueAndValidUntilBefore(currentDate.toString());

        // Iterate through the expired TravelPricesResponse records and delete corresponding reservations
        for (TravelPricesResponse response : expiredResponses) {
            // Find and delete reservations linked to this TravelPricesResponse
            List<Reservation> reservations = reservationRepository.findByValidUntil(response.getValidUntil());

            for (Reservation reservation : reservations) {
                // Deleting reservations, company-names, and routes should be cascaded by JPA.
                reservationRepository.delete(reservation);
            }
        }
    }



}
