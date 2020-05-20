package com.oc.batch;

import com.oc.batch.model.beans.*;
import com.oc.batch.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceImplTest {

    private EmailServiceImpl classUnderTest;
    private ReservationBean reservation;

    /**
     *
     */
    @BeforeEach
    public void init() {
        classUnderTest = new EmailServiceImpl();
        reservation = new ReservationBean();
        AvailableCopieBean availableCopieBean = new AvailableCopieBean();
        availableCopieBean.setId(new AvailableCopieKeyBean(1,1));
        BookBean book = new BookBean();
        availableCopieBean.setBook(book);
        LibraryBean library = new LibraryBean();
        availableCopieBean.setLibrary(library);
        RegisteredUserBean user = new RegisteredUserBean();
        reservation.setRegistereduser(user);
        reservation.setAvailableCopie(availableCopieBean);
        reservation.getAvailableCopie().getBook().setTitle("Toto");
        reservation.getAvailableCopie().getLibrary().setName("Graulhet");
        reservation.getRegistereduser().setEmail("ccathala.dev@gmail.com");

    }

    /**
     *
     */
    @AfterEach
    public void undef() {
        reservation = null;
    }

    @Test
    public void Given_reservationBean_When_generateReservationNotificationEmail_Then_shouldReturnEmailAddress() {
        // GIVEN
        // Init new line
        String newLine = System.getProperty("line.separator");
        // WHEN
        final HashMap<String, String> result = classUnderTest.generateReservationNotificationEmail(reservation);
        // THEN
        assertThat(result.get("to")).isEqualTo("ccathala.dev@gmail.com");
        }

    @Test
    public void Given_reservationBean_When_generateReservationNotificationEmail_Then_shouldReturnSubject() {
        // GIVEN
        // Init new line
        String newLine = System.getProperty("line.separator");
        // WHEN
        final HashMap<String, String> result = classUnderTest.generateReservationNotificationEmail(reservation);
        // THEN
        assertThat(result.get("subject")).isEqualTo("Réservation du livre: Toto");
        }

    @Test
    public void Given_reservationBean_When_generateReservationNotificationEmail_Then_shouldReturnText() {
        // GIVEN
        // Init new line
        String newLine = System.getProperty("line.separator");
        // WHEN
        final HashMap<String, String> result = classUnderTest.generateReservationNotificationEmail(reservation);
        // THEN
        assertThat(result.get("text")).isEqualTo("Le livre \"Toto\" que vous avez réservé est disponible à la bibliothèque de Graulhet vous disposez de 48h pour venir le récupérer, au delà la réservation sera annulée." + newLine + "Cordialement." + newLine + "OC-Bibliothèque.");
    }
}
