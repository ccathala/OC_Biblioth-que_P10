package com.oc.api.manager.impl;

import com.oc.api.dao.RegisteredUserDao;
import com.oc.api.manager.RegisteredUserManager;
import com.oc.api.model.beans.RegisteredUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisteredUserManagerImpl implements RegisteredUserManager {

    @Autowired
    private RegisteredUserDao registeredUserDao;

    @Override
    public List<RegisteredUser> findAll() {
        return registeredUserDao.findAll();
    }

    @Override
    public Optional<RegisteredUser> findById(int id) {
        return registeredUserDao.findById(id);
    }

    @Override
    public RegisteredUser save(RegisteredUser registeredUser) {
        return registeredUserDao.save(registeredUser);
    }

    @Override
    public void deleteById(int id) {
        registeredUserDao.deleteById(id);
    }

    @Override
    public RegisteredUser findByEmail(String email) {
        return registeredUserDao.findByEmail(email);
    }
}
