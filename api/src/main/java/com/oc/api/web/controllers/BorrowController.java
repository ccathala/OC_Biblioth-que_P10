package com.oc.api.web.controllers;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import com.oc.api.manager.BorrowManager;
import com.oc.api.model.beans.Borrow;
import com.oc.api.web.exceptions.ForeignKeyNotExistsException;
import com.oc.api.web.exceptions.RessourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;





/**
 * BorrowController
 * 
 * RestController, handle client request and provide entity Borrow data
 */
@RestController
@Validated
public class BorrowController {

    /*@Autowired
    private BorrowDao borrowDao;*/

    @Autowired
    private BorrowManager borrowManager;

    @Autowired
    private AvailableCopieController availableCopieController;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value="/borrows")
    public List<Borrow> getBorrows() {

        logger.info("Providing borrow resource from database: all borrow list");

        List<Borrow> borrows = borrowManager.getAllBorrows();

        return borrows;
    }

    @GetMapping(value="/borrows/{id}")
    public Borrow getBorrowById(@PathVariable @Min(value = 1) int id) {

        logger.info("Providing borrow resource from database: borrow id: " + id);

        Optional<Borrow> borrow = borrowManager.getById(id);

        if(!borrow.isPresent()) throw new RessourceNotFoundException("Le prêt n'existe pas, id: "+ id);

        return borrow.get();
    }

    @PostMapping(value="/borrows")
    @Transactional
    public ResponseEntity<Void> addBorrow(@Valid @RequestBody Borrow borrow) {

        logger.info("Adding new borrow in database");
          
        Borrow borrowAdded;
        try {
            borrowAdded = borrowManager.save(borrow, "out");
        } catch (Exception e) {
            logger.debug("Une ou plusieurs clé étrangères n'existent pas.");
            throw new ForeignKeyNotExistsException("Une ou plusieurs clé étrangères n'existent pas.");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(borrowAdded.getId())
                .toUri();
  
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value="/borrows/{id}")
    public ResponseEntity<Void> updateBorrow(@PathVariable @Min(value = 1) int id, @Valid @RequestBody Borrow borrowDetails) {
        
        logger.info("Updating borrow in database, id: " + id);

        try {
            borrowManager.getById(borrowDetails.getId()).get();
        } catch (NoSuchElementException e) {
            logger.debug("L'entité prêt demandée n'existe pas, id: " + borrowDetails.getId());
            throw new RessourceNotFoundException("L'entité prêt demandée n'existe pas, id: " + borrowDetails.getId());
        }
        
        borrowManager.save(borrowDetails , "");

        return ResponseEntity.ok().build();        

    }

    @PutMapping(value="/borrows/extend/{id}")
    public ResponseEntity<Void> extendBorrow(@PathVariable @Min(value = 1) int id, @Valid @RequestBody Borrow borrowDetails) {

        logger.info("Extend borrow in database, id: " + id);

        try {
            borrowManager.getById(borrowDetails.getId()).get();
        } catch (NoSuchElementException e) {
            logger.debug("L'entité prêt demandée n'existe pas, id: " + borrowDetails.getId());
            throw new RessourceNotFoundException("L'entité prêt demandée n'existe pas, id: " + borrowDetails.getId());
        }

        borrowManager.save(borrowDetails , "extend");

        return ResponseEntity.ok().build();

    }

    @PutMapping(value = "/borrows/return/{id}")
    @Transactional
    public ResponseEntity<Void> returnBorrow(@PathVariable @Min(value = 1) int id) {

        logger.info("Return borrow, id: " + id);

        Borrow returnedBorrow;
        try {
            returnedBorrow = borrowManager.getById(id).get();
        } catch (NoSuchElementException e) {
            logger.debug("L'entité prêt demandée n'existe pas, id: " + id);
            throw new RessourceNotFoundException("L'entité prêt demandée n'existe pas, id: " + id);
        }

        returnedBorrow.setBookReturned(true);
        borrowManager.save(returnedBorrow, "in");

        return ResponseEntity.ok().build();

    }
    
    @DeleteMapping(value="/borrows/{id}")
    public void deleteBorrow(@PathVariable @Min(value = 1) int id) {
        
        logger.info("Deleting borrow from database: id: "+ id);

        try {
            borrowManager.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("L'entité prêt n'existe pas, id: "+ id);
            throw new RessourceNotFoundException("L'entité prêt n'existe pas, id: "+ id);
        }
        
        
    }

    @GetMapping(value="/users/{user_id}/borrows")
    public List<Borrow> getBorrowsByUserId(@PathVariable(value = "user_id") @Min(value = 1) int userId) {

        logger.info("Providing borrow resources from database by user id: " + userId);

        List<Borrow>  borrows = borrowManager.getAllBorrowsByRegistereduserId(userId);

        return borrows;
    }  
    
}