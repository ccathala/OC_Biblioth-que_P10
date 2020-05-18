package com.oc.batch.web.proxies;

import java.util.List;

import com.oc.batch.model.beans.BorrowBean;

import com.oc.batch.model.beans.ReservationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * ApiProxi
 */
@FeignClient(name = "${api.name}", url = "${api.url}")
public interface ApiProxy {

    @GetMapping(value="/borrows")
    public List<BorrowBean> getBorrows();

    @GetMapping(value = "/reservations")
    public List<ReservationBean> getReservations();

    @DeleteMapping(value="/reservations/{id}")
    public void deleteReservation(@PathVariable @Min(value = 1) int id);

    @PutMapping(value = "/reservations/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable @Min(value = 1) int id, @Valid @RequestBody ReservationBean reservationDetails);
}