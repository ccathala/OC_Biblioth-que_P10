package com.oc.api.integration.dao;

import java.util.List;

import com.oc.api.model.beans.Borrow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * BorrowDao
 */
@Repository
public interface BorrowDao extends JpaRepository<Borrow, Integer>{

    List<Borrow> findByRegistereduserId(int userId);

    @Query(value = "SELECT * from public.borrow WHERE book_id = ? AND library_id = ?;", nativeQuery = true)
    List<Borrow> findAllByBookIdAndLibraryId(int book_id, int library_id);
}