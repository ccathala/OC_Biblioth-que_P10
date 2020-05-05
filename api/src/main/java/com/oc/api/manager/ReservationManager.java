package com.oc.api.manager;

import com.oc.api.model.beans.Reservation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReservationManager {
    void relatedReservationUpdate(int bookId, int libraryId, int userId);

    @Transactional
    void deleteById(int id);

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(int id);

    List<Reservation> findAllByBookIdAndLibraryId(int bookId, int libraryId);
}
