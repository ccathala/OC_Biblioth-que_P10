package com.oc.api.web.controllers;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import com.oc.api.dao.RegisteredUserDao;
import com.oc.api.manager.RegisteredUserManager;
import com.oc.api.model.beans.RegisteredUser;
import com.oc.api.web.exceptions.EntityAlreadyExistsException;
import com.oc.api.web.exceptions.RessourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * RegisteredUserController
 */
@RestController()
@Validated
public class RegisteredUserController {

    @Autowired
    private RegisteredUserManager registeredUserManager;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/users")
    public List<RegisteredUser> getRegisteredUsers() {

        logger.info("Providing registeredUser resource from database: all registeredUser list");

        List<RegisteredUser> users = registeredUserManager.findAll();

        return users;
    }

    @GetMapping(value = "/users/{id}")
    public Optional<RegisteredUser> getRegisteredUsersById(@PathVariable @Min(value = 1) int id) {

        logger.info("Providing registeredUser resource from database: registeredUser id: " + id);

        Optional<RegisteredUser> user = registeredUserManager.findById(id);

        if (!user.isPresent())
            throw new RessourceNotFoundException("L'entité utilisateur n'existe pas, id: " + id);

        return user;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Void> addRegisteredUser(@Valid @RequestBody RegisteredUser registeredUser) {

        logger.info("Adding new registeredUser in database");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        RegisteredUser newUser = registeredUser;
        newUser.setPassword(passwordEncoder.encode(registeredUser.getPassword()));

        RegisteredUser registeredUserAdded = null;
        try {
            registeredUserAdded = registeredUserManager.save(newUser);
        } catch (Exception e) {
            logger.debug("Cette adresse email est déjà liée à un compte utilisateur: " + registeredUser.getEmail());
            throw new EntityAlreadyExistsException(
                    "Cette adresse email est déjà liée à un compte utilisateur: " + registeredUser.getEmail());
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(registeredUserAdded.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<Void> updateRegisteredUser(@PathVariable @Min(value = 1) int id,
            @Valid @RequestBody RegisteredUser registeredUserDetails) {

        logger.info("Updating registeredUser in database, id: " + id);

        try {
            registeredUserManager.findById(registeredUserDetails.getId()).get();
        } catch (NoSuchElementException e) {
            logger.debug("L'entité utilisateur demandée n'existe pas, id " + registeredUserDetails.getId());
            throw new RessourceNotFoundException(
                    "L'entité utilisateur demandée n'existe pas, id " + registeredUserDetails.getId());
        }

        registeredUserManager.save(registeredUserDetails);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping(value = "/users/{id}")
    public void deleteRegisteredUser(@PathVariable @Min(value = 1) int id) {

        logger.info("Deleting registeredUser from database: id: " + id);

        try {
            registeredUserManager.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("L'entité utilisateur n'existe pas, id: " + id);
            throw new RessourceNotFoundException("L'entité utilisateur n'existe pas, id: " + id);
        }

    }

    @PostMapping(value = "/users/email")
    public RegisteredUser findUserByEmail(@RequestBody String email) {

        logger.info("Search user by email: " + email);

        RegisteredUser user = new RegisteredUser();

        user = registeredUserManager.findByEmail(email);

        return user;
    }

}