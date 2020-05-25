package com.oc.api.unit;

import com.oc.api.dao.ReservationDao;
import com.oc.api.manager.AvailableCopieManager;
import com.oc.api.manager.BorrowManager;
import com.oc.api.manager.ReservationManager;
import com.oc.api.manager.impl.OutDatedReservationManagerImpl;
import com.oc.api.manager.impl.ReservationManagerImpl;
import com.oc.api.model.beans.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OutDatedReservationManagerImplTest {

    private OutDatedReservationManagerImpl classUnderTest;
    @MockBean
    private ReservationManager reservationManager;
    private AvailableCopie availableCopie;
    private List<Borrow> borrowList;
    private Borrow borrow;
    private List<Reservation> reservationList;
    private Reservation reservation;

    /**
     *
     */
    @BeforeEach
    public void init() {
        classUnderTest = new OutDatedReservationManagerImpl();
        classUnderTest.setReservationManager(reservationManager);
        /*availableCopie = new AvailableCopie();
        availableCopie.setOwnedQuantity(2);
        availableCopie.setReservationCount(4);
        borrowList = new ArrayList<>();
        borrow = new Borrow();
        borrow.setBookReturned(false);*/
        /*Book book = new Book();
        book.setId(1);
        borrow.setBook(book);
        borrowList.add(borrow);*/
        reservationList = new ArrayList<>();
        reservation = new Reservation();
        AvailableCopie availableCopie = new AvailableCopie();
        availableCopie.setId(new AvailableCopieKey(1,1));
        reservation.setAvailableCopie(availableCopie);
        reservationList.add(reservation);
    }

    /**
     *
     */
    @AfterEach
    public void undef() {
        classUnderTest = null;
        availableCopie = null;
        borrow = null;
        borrowList = null;
        reservationList = null;
        reservation = null;
    }

    @Test
    public void Given__When__Then_() {
        // GIVEN
        Reservation outDatedReservation = new Reservation();
        outDatedReservation.setAvailabilityDate(LocalDate.of(2020,1,1));
        outDatedReservation.setNotificationIsSent(true);
        reservationList.add(outDatedReservation);
        given(reservationManager.findAll()).willReturn(reservationList);
        doNothing().when(reservationManager).deleteById(1);
        // WHEN
        final List<Reservation> result = classUnderTest.deleteOutDatedReservations();
        // THEN
        assertThat(result.size()).isEqualTo(1);
    }
}
