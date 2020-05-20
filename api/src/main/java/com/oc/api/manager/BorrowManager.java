package com.oc.api.manager;

import com.oc.api.model.beans.Borrow;
import com.oc.api.web.exceptions.FunctionnalException;

import java.util.List;
import java.util.Optional;

public interface BorrowManager {

    List<Borrow> getAllBorrows();
    Optional<Borrow> getById(int id);
    Borrow save(Borrow borrow, String operationType) throws FunctionnalException;
    void deleteById(int id);
    List<Borrow> getAllBorrowsByRegistereduserId(int userId);
    List<Borrow> getAllBorrowsByBookIdAndLibraryId(int book_id, int library_id);
}
