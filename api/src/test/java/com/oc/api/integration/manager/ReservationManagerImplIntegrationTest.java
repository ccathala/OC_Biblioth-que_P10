package com.oc.api.integration.manager;

import com.oc.api.manager.impl.ReservationManagerImpl;
import com.oc.api.model.beans.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReservationManagerImplIntegrationTest {

    @Autowired
    private ReservationManagerImpl classUnderTest;


    @Test
    @Transactional
    public void Given_existingReservation_When_relatedReservationUpdate_Then_shouldReturnListSizeLessOne() {
        // GIVEN
        List<Reservation> reservationList = classUnderTest.findAll();
        int listSize = reservationList.size();
        // WHEN
        classUnderTest.relatedReservationUpdate(1,1, 3);
        // THEN
        List<Reservation> reservationListAfterUpdate = classUnderTest.findAll();
        int listSizeAfterUpdate = reservationListAfterUpdate.size();
        assertThat(listSizeAfterUpdate).isEqualTo(listSize - 1);
    }

    @Test
    @Transactional
    public void Given_notExistingReservation_When_relatedReservationUpdate_Then_shouldReturnSameListSize() {
        // GIVEN
        List<Reservation> reservationList = classUnderTest.findAll();
        int listSize = reservationList.size();
        // WHEN
        classUnderTest.relatedReservationUpdate(1,1, 4);
        // THEN
        List<Reservation> reservationListAfterUpdate = classUnderTest.findAll();
        int listSizeAfterUpdate = reservationListAfterUpdate.size();
        assertThat(listSizeAfterUpdate).isEqualTo(listSize);
    }

    @Test
    @Transactional
    public void Given_reservationBookIdIs1AndLibraryIdIs1_When_offsetReservationsPositionAfterDelete_Then_shouldReturnPositionLess1() {
        // GIVEN

        // WHEN
        classUnderTest.offsetReservationsPositionAfterDelete(1,1);
        // THEN
        List<Reservation> reservationList = classUnderTest.findAll();
        assertThat(reservationList.get(0).getPosition()).isEqualTo(0);

    }
}
