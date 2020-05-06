package com.oc.api.manager.impl;

import com.oc.api.integration.dao.ReservationDao;
import com.oc.api.manager.AvailableCopieManager;
import com.oc.api.manager.ReservationManager;
import com.oc.api.model.beans.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationManagerImpl implements ReservationManager {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private AvailableCopieManager availableCopieManager;

    /**
     *
     */
    @Override
    public void relatedReservationUpdate(int bookId, int libraryId, int userId) {
        Optional<Reservation> relatedReservation = reservationDao.findByBookAndLibraryAndRegisteredUser(bookId, libraryId, userId);

        if (relatedReservation.isPresent()) {
            deleteById(relatedReservation.get().getId());
        }
    }

    /**
     *
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        Reservation reservationToDelete = reservationDao.findById(id).get();
        int bookId = reservationToDelete.getAvailableCopie().getId().getBookId();
        int libraryid = reservationToDelete.getAvailableCopie().getId().getLibraryId();
        reservationDao.deleteById(id);
        offsetReservationsPositionAfterDelete(bookId, libraryid);
        availableCopieManager.updateReservationCount(reservationToDelete.getAvailableCopie().getId().getBookId(), reservationToDelete.getAvailableCopie().getId().getLibraryId());
    }

    /**
     * After delete reservation action, decrease by one each reservations positions for the same copy
     */
    public void offsetReservationsPositionAfterDelete(int bookId, int libraryId) {
        List<Reservation> reservationList =
                findAllByBookIdAndLibraryId(bookId, libraryId);

        for (Reservation reservation : reservationList) {
            int currentPosition = reservation.getPosition();
            reservation.setPosition(currentPosition - 1);
            reservationDao.save(reservation);
        }
    }

    /**
     *
     */
    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        int reservationPosition = setReservationPosition(reservation);
        reservation.setPosition(reservationPosition);
        Reservation savedReservation = reservationDao.save(reservation);
        availableCopieManager.updateReservationCount(reservation.getAvailableCopie().getId().getBookId(), reservation.getAvailableCopie().getId().getLibraryId());
        return savedReservation;
    }

    /**
     *
     */
    private int setReservationPosition(Reservation reservation) {
        if (reservation.getPosition() == 0) {
            List<Reservation> reservationList =
                    reservationDao.findAllByBookIdAndLibraryId(reservation.getAvailableCopie().getBook().getId(),
                            reservation.getAvailableCopie().getLibrary().getId());

            return reservationList.size() + 1;

        }
        return reservation.getPosition();
    }

    /**
     *
     */
    @Override
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    /**
     *
     */
    @Override
    public Optional<Reservation> findById(int id) {
        return reservationDao.findById(id);
    }

    /**
     *
     */
    @Override
    public List<Reservation> findAllByBookIdAndLibraryId(int bookId, int libraryId) {
        return reservationDao.findAllByBookIdAndLibraryId(bookId, libraryId);
    }
}
