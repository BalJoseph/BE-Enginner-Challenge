package com.yieldstreet.josephbaldacchino.takehomechallenge.controller;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.AccreditationCreationModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.AccreditationCreationResponseModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationsretrieval.UserAccreditationsResponseModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationstatusfinalization.AccreditationStatusFinalizationModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationstatusfinalization.AccreditationStatusFinalizationResponseModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Document;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.User;
import com.yieldstreet.josephbaldacchino.takehomechallenge.service.DatabaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class AccreditationController {

    private final DatabaseService dbService;

    public AccreditationController(DatabaseService dbService){
        this.dbService = dbService;
    }

    @PostMapping("/user/accreditation")
    public ResponseEntity<AccreditationCreationResponseModel> CreateAccreditation(@RequestBody @Valid AccreditationCreationModel acm){

        //Validate the user
        User user = dbService.getUser(acm.getUser_id());
        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        //Check if the user already has a pending accreditation
        if(dbService.userHasPendingAccreditation(user.getId())){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "User already has a 'PENDING' accreditation");
        }

        //Process the data
        Accreditation newAccreditation = new Accreditation(acm);
        dbService.addAccreditation(newAccreditation);

        //add it to the user
        dbService.addUserAccreditation(user.getId(), newAccreditation.getId());

        //Return the response
        AccreditationCreationResponseModel acrm = new AccreditationCreationResponseModel(newAccreditation);
        return new ResponseEntity<>(acrm, HttpStatus.CREATED);
    }

    @PutMapping("/user/accreditation/{accreditationId}")
    public ResponseEntity<AccreditationStatusFinalizationResponseModel> UpdateAccreditationDocumentStatus(@PathVariable String accreditationId, @RequestBody @Valid AccreditationStatusFinalizationModel asfm){

        Accreditation.StatusEnum requestedOutcome = Accreditation.StatusEnum.valueOf(asfm.getOutcome());

        //Check if the accreditation id exists
        Accreditation accreditation = dbService.getAccreditation(accreditationId);
        if(accreditation == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Accreditation.StatusEnum currentStatus = accreditation.getStatus();

        //Ensure that the requested change is permitted
        if(currentStatus == Accreditation.StatusEnum.EXPIRED){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "An 'EXPIRED' accreditation status should not be updated further");
        }

        if(currentStatus == Accreditation.StatusEnum.FAILED){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "A 'FAILED' accreditation status should not be updated further");
        }

        if(currentStatus == Accreditation.StatusEnum.CONFIRMED && requestedOutcome != Accreditation.StatusEnum.EXPIRED){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "A 'CONFIRMED' accreditation status can only be updated to 'EXPIRED'");
        }

        //Perform the update
        accreditation.setStatus(requestedOutcome);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String updateTime = currentDateTime.format(formatter);

        accreditation.setLastUpdate(updateTime);

        //return response
        AccreditationStatusFinalizationResponseModel asfrm = new AccreditationStatusFinalizationResponseModel(accreditation);
        return new ResponseEntity<>(asfrm, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/accreditation")
    public ResponseEntity<UserAccreditationsResponseModel> GetAccreditationByUser(@PathVariable String userId){
        List<Accreditation> userAccreditations = new ArrayList<>();

        User user = dbService.getUser(userId);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        for(String accreditationId : dbService.getUserAccreditations(user.getId())){
            Accreditation accreditation = dbService.getAccreditation(accreditationId);

            //Check if not null first
            if(accreditation == null){
                continue;
            }

            userAccreditations.add(accreditation);
        }

        UserAccreditationsResponseModel uarm = new UserAccreditationsResponseModel(user.getId(), userAccreditations);
        return new ResponseEntity<>(uarm, HttpStatus.OK);
    }
}
