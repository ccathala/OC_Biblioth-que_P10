package com.oc.webapp.service;

import java.util.List;

import com.oc.webapp.model.beans.AvailableCopieBean;
import com.oc.webapp.model.beans.BorrowBean;
import com.oc.webapp.model.beans.ReservationBean;
import com.oc.webapp.model.dto.RegisteredUserDto;
import org.springframework.http.ResponseEntity;

/**
 * WebappService
 */
public interface WebappService {

    ResponseEntity<Void> createUser(RegisteredUserDto accountDto);

    int getAuthenticatedRegisteredUserId();

    Boolean getIsAuthenticated();

    /*ResponseEntity<Void> extendBorrowDuration(int borrowId);*/

    List<BorrowBean> getActiveBorrowsByRegisteredUserId();

    List<ReservationBean> getReservationsByRegisteredUserId();

    ReservationBean createReservation(int bookId, int libraryId);

    List<Integer> getBookIdReservationList(int authenticatedUserId);

    List<Integer> getBookIdActiveBorrowList();

    List<AvailableCopieBean> getAvailableCopies();
}