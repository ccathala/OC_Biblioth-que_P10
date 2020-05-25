package com.oc.api.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.api.manager.impl.ReservationManagerImpl;
import com.oc.api.model.beans.Reservation;
import com.oc.api.web.exceptions.FunctionnalException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class ReservationManagerImplIntegrationTest {

    @Autowired
    private ReservationManagerImpl classUnderTest;

    private ObjectMapper mapper;
    private JSONParser jsonParser;
    private Reader reader;
    private JSONObject reservationJSON;
    private Reservation reservation;

    @BeforeEach
    public void initBeforeEach() throws IOException, ParseException {
        mapper = new ObjectMapper();
        jsonParser = new JSONParser();
        reader = new FileReader(getClass().getClassLoader().getResource("json/reservation.json").getFile());
        reservationJSON = (JSONObject) jsonParser.parse(reader);
        reservation = new Reservation();
        File file = new File(getClass().getClassLoader().getResource("json/reservation.json").getFile());
        reservation = mapper.readValue(file, Reservation.class);
    }


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
    public void Given_reservationBookIdIs1AndLibraryIdIs1AndPositionIs2_When_offsetReservationsPositionAfterDelete_Then_shouldReturnIndex0PositionEqual1() {
        // GIVEN

        // WHEN
        classUnderTest.offsetReservationsPositionAfterDelete(1,1, 2);
        // THEN
        List<Reservation> reservationList = classUnderTest.findAll();
        assertThat(reservationList.get(0).getPosition()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void Given_reservationBookIdIs1AndLibraryIdIs1AndPositionIs1_When_offsetReservationsPositionAfterDelete_Then_shouldReturnIndex1Position1() {
        // GIVEN

        // WHEN
        classUnderTest.offsetReservationsPositionAfterDelete(1,1, 1);
        // THEN
        List<Reservation> reservationList = classUnderTest.findAll();
        assertThat(reservationList.get(1).getPosition()).isEqualTo(1);

    }

    @Test
    public void Given_reservationPositionIsEqualTo1_When_setReservationPosition_Then_shouldReturn1() {
        // GIVEN

        // WHEN
        final int result = classUnderTest.setReservationPosition(1,1,1);
        // THEN
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void Given_reservationPositionIsEqualTo0_When_setReservationPosition_Then_shouldReturn3() {
        // GIVEN

        // WHEN
        final int result = classUnderTest.setReservationPosition(1,1,0);
        // THEN
        assertThat(result).isEqualTo(3);
    }

    
    @Test
    @Transactional
    public void Given_newReservationBean_When_save_Then_shouldReturnReservationListSizeMoreOne() throws FunctionnalException {
        // GIVEN
        Reservation reservationToSave = new Reservation();
        reservationToSave.setPosition(reservation.getPosition());
        reservationToSave.setAvailabilityDate(reservation.getAvailabilityDate());
        reservationToSave.setNotificationIsSent(reservation.getNotificationIsSent());
        reservationToSave.setAvailableCopie(reservation.getAvailableCopie());
        reservationToSave.setRegistereduser(reservation.getRegistereduser());

        int reservationCount = classUnderTest.findAll().size();
        // WHEN
        classUnderTest.save(reservationToSave);
        // THEN
        int reservationCountAfterSave = classUnderTest.findAll().size();
        assertThat(reservationCountAfterSave).isEqualTo(reservationCount + 1);
    }

    @Test
    public void Given_reservationBeanId_When_findById_Then_shouldReturnReservationBeanWithIdIs1() {
        // GIVEN

        // WHEN
        final Reservation result = classUnderTest.findById(1).get();
        // THEN
        assertThat(result.getId()).isEqualTo(1);
    }
}
