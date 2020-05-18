package com.oc.api.manager.impl;

import com.oc.api.dao.BorrowDao;
import com.oc.api.manager.AvailableCopieManager;
import com.oc.api.manager.BorrowManager;
import com.oc.api.manager.ReservationManager;
import com.oc.api.model.beans.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowManagerImpl implements BorrowManager {

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private AvailableCopieManager availableCopieManager;

    @Autowired
    private ReservationManager reservationManager;

    @Override
    public List<Borrow> getAllBorrows() {
        return borrowDao.findAll();
    }

    @Override
    public Optional<Borrow> getById(int id) {
        return borrowDao.findById(id);
    }

    @Override
    @Transactional
    public Borrow save(Borrow borrow, String operationType) {

        int bookId = borrow.getBook().getId();
        int libraryId = borrow.getLibrary().getId();
        int userId = borrow.getRegistereduser().getId();

        if (operationType.equals("extend")){
            // Add 4 weeks to return date
            borrow.setReturnDate(borrow.getReturnDate().plusWeeks(4));

            // Set true extended duration attribute
            borrow.setExtendedDuration(true);
        }

        // Save or update borrow
        Borrow savedBorrow = borrowDao.save(borrow);

        // Update related availableCopie, triggered by borrow action(add or return)
        availableCopieManager.relatedAvailableCopieUpdate(bookId, libraryId, operationType);

        // If outing borrow need to check
        if(operationType.equals("out")){
            // Update related reservation
            reservationManager.relatedReservationUpdate(bookId, libraryId, userId);
        }

        return savedBorrow;
    }

    @Override
    public void deleteById(int id) {
        borrowDao.deleteById(id);
    }

    @Override
    public List<Borrow> getAllBorrowsByRegistereduserId(int userId) {
        return borrowDao.findByRegistereduserId(userId);
    }

    @Override
    public List<Borrow> getAllBorrowsByBookIdAndLibraryId(int book_id, int library_id) {
        return borrowDao.findAllByBookIdAndLibraryId(book_id, library_id);
    }

}
