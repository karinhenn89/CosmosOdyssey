package com.example.CosmosOdyssey.Service;


import com.example.CosmosOdyssey.Model.Reservation;
import com.example.CosmosOdyssey.Repository.ReservationRepositroy;
import com.example.CosmosOdyssey.Repository.TravelPricesResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ReservationService {

    @Autowired
    private ReservationRepositroy reservationRepository;

    @Autowired
    private TravelPricesResponseRepository travelPricesResponseRepository;

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }



}
