package com.oc.api.integration.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.api.manager.impl.AvailableCopieManagerImpl;
import com.oc.api.model.beans.AvailableCopie;
import com.oc.api.model.beans.AvailableCopieKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AvailableCopieManagerImplIntegrationTest {

    @Autowired
    private AvailableCopieManagerImpl classUnderTest;

    ObjectMapper mapper;

    /**
     * Init before each test
     */
    @BeforeEach
    public void init() {
        mapper = new ObjectMapper();
    }


    @Test
    public void Given_2BorrowsInDatabase_When_getNearestReturnDate_Then_shouldReturnTrue() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(1, 1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-02-01");
    }

    @Test
    public void Given_1BorrowInDatabase_When_getNearestReturnDate_Then_shouldReturnTrue() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(2, 1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-02-14");
    }

    @Test
    public void Given_availableCopieId_When_findById_Then_returnBeanwithSameId() throws JsonProcessingException {
        // GIVEN

        // WHEN
        final AvailableCopie result = classUnderTest.findById(new AvailableCopieKey(1, 1)).get();
        // THEN
        assertThat(result.getId()).isEqualTo(new AvailableCopieKey(1,1));
    }

    @Test
    @Transactional
    public void Given_reservationBookIdAndLibraryId_When_updateReservationCount_Then_shouldReturn2() {
        // GIVEN

        // WHEN
        final AvailableCopie result = classUnderTest.updateReservationCount(1,1);
        // THEN
        assertThat(result.getReservationCount()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void Given_availableCopieIdBookId1AndLibraryId1AndOperationTypeIsIn_When_relatedAvailableCopieUpdate_Then_shouldReturnAvailableQuantityIs1() {
        // GIVEN

        // WHEN
        final AvailableCopie result = classUnderTest.relatedAvailableCopieUpdate(1,1, "in");
        // THEN
        assertThat(result.getAvailableQuantity()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void Given_availableCopieIdBookId1AndLibraryId1AndOperationTypeIsIn_When_relatedAvailableCopieUpdate_Then_shouldReturnNearestReturnDateIs20200201() {
        // GIVEN

        // WHEN
        final AvailableCopie result = classUnderTest.relatedAvailableCopieUpdate(1,1, "in");
        // THEN
        assertThat(result.getNearestReturnDate().toString()).isEqualTo("2020-02-01");

    }

    @Test
    @Transactional
    public void Given_availableCopieIdBookId2AndLibraryId1AndOperationTypeIsOut_When_relatedAvailableCopieUpdate_Then_shouldReturnAvailableQuantityIs0() {
        // GIVEN

        // WHEN
        final AvailableCopie result = classUnderTest.relatedAvailableCopieUpdate(2,1, "out");
        // THEN
        assertThat(result.getAvailableQuantity()).isEqualTo(0);

    }

    @Test
    @Transactional
    public void Given_availableCopieIdBookId2AndLibraryId1AndOperationTypeIsOut_When_relatedAvailableCopieUpdate_Then_shouldReturnNearestReturnDateIs20200214() {
        // GIVEN

        // WHEN
        final AvailableCopie result = classUnderTest.relatedAvailableCopieUpdate(2,1, "out");
        // THEN
        assertThat(result.getNearestReturnDate().toString()).isEqualTo("2020-02-14");

    }


}
