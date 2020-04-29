package com.oc.api.web.controllers;

import com.oc.api.dao.ReservationDao;
import com.oc.api.model.beans.Reservation;
import com.oc.api.web.exceptions.RessourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@Validated
public class ReservationController {

    @Autowired
    private ReservationDao reservationDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/reservations")
    public List<Reservation> getReservations() {

        logger.info("Providing reservation resource from database: all reservation list");
        List<Reservation> reservationList = reservationDao.findAll();

        return reservationList;
    }

    @GetMapping(value = "/reservations/{id}")
    public Optional<Reservation> getReservationById(@PathVariable @Min(value = 1) int id) {

        logger.info("Providing reservation resource from database: reservation id: " + id);
        Optional<Reservation> reservation = reservationDao.findById(id);

        if (!reservation.isPresent())
            throw new RessourceNotFoundException("L'entité réservation n'existe pas, id: " + id);

        return reservation;
    }

    @PostMapping(value = "/reservations")
    public ResponseEntity<Void> addReservation(@Valid @RequestBody Reservation reservation) {

        logger.info("Adding new reservation in database");
        Reservation reservationAdded;
        reservationAdded = reservationDao.save(reservation);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservation.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/reservations/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable @Min(value = 1) int id, @Valid @RequestBody Reservation reservationDetails) {

        logger.info("Updating reservation in database, id: " + id);

        try {
            reservationDao.findById(reservationDetails.getId()).get();
        }catch (NoSuchElementException e){
            logger.debug("L'entité réservation demandé n'existe pas, id: " + reservationDetails.getId());
            throw new RessourceNotFoundException("L'entité réservation demandé n'existe pas, id: " + reservationDetails.getId());
        }

        reservationDao.save(reservationDetails);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/reservations/{id}")
    public void deleteReservation(@PathVariable @Min(value = 1) int id) {

        logger.info("Deleting reservation from database: id: "+ id);

        try {
            reservationDao.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            logger.debug("L'entité réservation n'existe pas, id: " + id);
            throw new RessourceNotFoundException("L'entité réservation n'existe pas, id: " + id);
        }

    }


}
