package com.oc.api;

import com.oc.api.manager.impl.AvailableCopieManagerImpl;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AvailableCopieManagerIntegrationTest {

    @Autowired
    private AvailableCopieManagerImpl classUnderTest;


    @Test
    public void Given_2BorrowsInDatabase_When_getNearestReturnDate_Then_shouldReturnTrue() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(1,1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-02-01");
    }

    @Test
    public void Given_1BorrowInDatabase_When_getNearestReturnDate_Then_shouldReturnTrue() {
        // GIVEN

        // WHEN
        final LocalDate result = classUnderTest.getNearestReturnDate(2,1);
        // THEN
        assertThat(result.toString()).isEqualTo("2020-02-14");
    }
}
