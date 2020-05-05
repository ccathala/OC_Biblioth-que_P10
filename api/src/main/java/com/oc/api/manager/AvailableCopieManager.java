package com.oc.api.manager;

import com.oc.api.model.beans.AvailableCopie;
import com.oc.api.model.beans.AvailableCopieKey;
import com.oc.api.model.beans.Borrow;

import java.util.Optional;

public interface AvailableCopieManager {

    Boolean updateStatusBookCanBeReserved(AvailableCopie availableCopie);
    int updateAvailableQuantity(int currentQuantity, String operationType);

    void updateReservationCount(int bookId, int libraryId);

    Optional<AvailableCopie> findById(AvailableCopieKey key);
    AvailableCopie save(AvailableCopie availableCopie);

    AvailableCopie relatedAvailableCopieUpdate(Borrow borrow, String operationType);
}
