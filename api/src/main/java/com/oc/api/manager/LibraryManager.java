package com.oc.api.manager;

import com.oc.api.model.beans.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryManager {

    List<Library> findAll();
    Optional<Library> findById(int id);
    Library save(Library library);
    void deleteById(int id);
}

