package com.oc.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.api.manager.OutDatedReservationManager;
import com.oc.api.manager.ReservationManager;
import com.oc.api.model.beans.Reservation;
import com.oc.api.web.controllers.ReservationController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
@WithMockUser(username = "OCBibliotheque-client", password = "OCB2020")
public class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OutDatedReservationManager outDatedReservationManager;

    @MockBean
    private ReservationManager reservationManager;

    private Reservation reservation;
    private ObjectMapper mapper;
    private JSONParser jsonParser;
    private Reader reader;
    private JSONObject reservationJSON;

    @BeforeEach
    public void initBeforeEach() throws IOException, ParseException {
        reservation = new Reservation();
        reservation.setId(1);
        mapper = new ObjectMapper();
        jsonParser = new JSONParser();
        reader = new FileReader(getClass().getClassLoader().getResource("json/reservation.json").getFile());
        reservationJSON = (JSONObject) jsonParser.parse(reader);
    }

    @Test
    public void Given_reservationList_When_performGetReservations_Then_shouldReturnStatus200AndSize1() throws Exception {
        // GIVEN
        List<Reservation> reservationList = Arrays.asList(reservation);
        given(reservationManager.findAll()).willReturn(reservationList);
        // WHEN

        // THEN
        mockMvc.perform(get("/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void Given_reservationBean_When_performGetReservationId_Then_shouldReturnStatus200AndId1() throws Exception {
        // GIVEN
        Optional<Reservation> reservationOptional = Optional.ofNullable(reservation);
        given(reservationManager.findById(1)).willReturn(reservationOptional);
        // WHEN

        // THEN
        mockMvc.perform(get("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reservation.getId())));
    }

    @Test
    public void Given_reservationIdNotFound_When_getReservationId_Then_shouldReturnError4XX() throws Exception {
        // GIVEN
        reservation = null;
        given(reservationManager.findById(1)).willReturn(Optional.ofNullable(reservation));
        // WHEN

        // THEN
        mockMvc.perform(get("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }



    @Test
    public void Given_reservationBeanJson_When_performPostReservations_Then_shouldReturnIStatus201() throws Exception {
        // GIVEN

        given(reservationManager.save(reservation)).willReturn(reservation);
        // WHEN

        // THEN
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(reservationJSON)))
                .andExpect(status().isCreated());
    }

    @Test
    public void Given_reservationBeanJson_When_performPutReservationsId_Then_shouldReturnStatus200() throws Exception {
        // GIVEN
        given(reservationManager.save(reservation)).willReturn(reservation);
        given(reservationManager.findById(1)).willReturn(Optional.ofNullable(reservation));
        // WHEN

        // THEN
        mockMvc.perform(put("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(reservationJSON)))
                .andExpect(status().isOk());
    }

    @Test
    public void Given_reservationIdNotFound_When_performPutReservationId_Then_shouldReturnError4XX() throws Exception {
        // GIVEN
        reservation = null;
        given(reservationManager.findById(1)).willReturn(Optional.ofNullable(reservation));
        // WHEN

        // THEN
        mockMvc.perform(put("/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(reservationJSON)))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void Given_idToDelete_When_performDeleteReservationsId_Then_shouldReturnStatus200() throws Exception {
        // GIVEN

        // WHEN

        // THEN
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void Given_idToDelete_When_performDeleteReservationId_Then_shouldReturnStatus4XX() throws Exception {
        // GIVEN
        doThrow(EmptyResultDataAccessException.class).when(reservationManager).deleteById(1);
        // WHEN

        // THEN
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().is4xxClientError());
    }

}
