package com.example.CosmosOdyssey.Controller;

import com.example.CosmosOdyssey.Model.Reservation;
import com.example.CosmosOdyssey.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    @PostMapping("/add")
    public ResponseEntity<Reservation> creatReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.saveReservation(reservation));
    }

    @GetMapping("/all-reservations")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }



}
