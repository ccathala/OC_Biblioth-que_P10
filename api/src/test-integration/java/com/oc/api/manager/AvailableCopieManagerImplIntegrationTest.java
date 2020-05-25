package com.oc.api.manager;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
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
    public void Given_availableCopie_When_getNearestReturnDate_Then_shouldReturn20200201() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(1, 1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-02-01");
    }

    @Test
    public void Given_availableCopie_When_getNearestReturnDate_Then_shouldReturn20200214() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(2, 1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-02-14");
    }

    @Test
    public void Given_availableCopie_When_getNearestReturnDate_Then_shouldReturn202005() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(3, 1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-05-14");
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

    @Test
    public void Given_9availableCopieRecordInDatabase_When_findAll_Then_shouldReturn9() {
        // GIVEN

        // WHEN
        final List<AvailableCopie> result  = classUnderTest.findAll();
        // THEN
        assertThat(result.size()).isEqualTo(9);
    }

    @Test
    public void Given_existingAvailableCopieId_When_existsById_Then_shouldReturnTrue() {
        // GIVEN

        // WHEN
        final Boolean result = classUnderTest.existsById(new AvailableCopieKey(1,1));
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void Given_notExistingAvailableCopieId_When_existsById_Then_shouldReturnFalse() {
        // GIVEN

        // WHEN
        final Boolean result = classUnderTest.existsById(new AvailableCopieKey(10,1));
        // THEN
        assertThat(result).isFalse();
    }

    @Test
    public void Given_availableCopieBean_When_deleteById_Then_shouldReturnListSizeLess1() {
        // GIVEN
        AvailableCopie availableCopie = classUnderTest.findById(new AvailableCopieKey(3,3)).get();
        int availableCopieCountBeforeDelete = classUnderTest.findAll().size();
        // WHEN
        classUnderTest.deleteById(new AvailableCopieKey(3,3));
        final int result = classUnderTest.findAll().size();
        classUnderTest.save(availableCopie);
        // THEN
        assertThat(result).isEqualTo(availableCopieCountBeforeDelete - 1);
    }

}
