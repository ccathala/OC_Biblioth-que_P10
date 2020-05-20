package com.oc.api.manager;

import com.oc.api.model.beans.Reservation;

import java.util.List;

public interface OutDatedReservationManager {
    List<Reservation> deleteOutDatedReservations();
}
