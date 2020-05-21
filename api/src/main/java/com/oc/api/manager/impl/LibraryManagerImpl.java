package com.oc.api.manager.impl;

import com.oc.api.dao.LibraryDao;
import com.oc.api.manager.LibraryManager;
import com.oc.api.model.beans.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryManagerImpl implements LibraryManager {

    @Autowired
    private LibraryDao libraryDao;

    @Override
    public List<Library> findAll() {
        return libraryDao.findAll();
    }

    @Override
    public Optional<Library> findById(int id) {
        return libraryDao.findById(id);
    }

    @Override
    public Library save(Library library) {
        return libraryDao.save(library);
    }

    @Override
    public void deleteById(int id) {
        libraryDao.deleteById(id);
    }
}
