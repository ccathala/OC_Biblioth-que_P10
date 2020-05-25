package com.oc.api.manager;

import com.oc.api.model.beans.RegisteredUser;

import java.util.List;
import java.util.Optional;

public interface RegisteredUserManager {

    List<RegisteredUser> findAll();
    Optional<RegisteredUser> findById(int id);
    RegisteredUser save(RegisteredUser registeredUser);
    void deleteById(int id);
    RegisteredUser findByEmail(String email);

}
