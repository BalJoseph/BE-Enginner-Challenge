package com.yieldstreet.josephbaldacchino.takehomechallenge.service;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Document;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.User;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.UserAccreditation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    //Hashmap of users data, with userId as key
    private Map<String, User> userTable = new HashMap<>();

    //Hashmap of accreditations with, accreditationId as key
    private Map<String, Accreditation> accreditationTable = new HashMap<>();

    //Connects the users to their respective accreditations
    private List<UserAccreditation> userAccreditationsTable = new ArrayList<>();


    public DatabaseService(){
        //Init basic database

        //Accreditations
        Document doc1 = new Document("doc1.pdf", "application/pdf", "lorem ipsum");

        //I'm reusing the same doc because it doesn't really make a difference

        //Pending
        Accreditation pendingAccreditation = new Accreditation("pending", Accreditation.AccreditationTypeEnum.BY_INCOME, doc1);
        addAccreditation(pendingAccreditation);

        //Confirmed
        Accreditation confirmedAccreditation = new Accreditation("confirmed", Accreditation.AccreditationTypeEnum.BY_NET_WORTH, doc1);
        confirmedAccreditation.setStatus(Accreditation.StatusEnum.CONFIRMED);
        addAccreditation(confirmedAccreditation);

        //Confirmed but past expiry date
        Accreditation oldConfirmedAccreditation = new Accreditation("oldConfirmed", Accreditation.AccreditationTypeEnum.BY_INCOME, doc1);
        oldConfirmedAccreditation.setStatus(Accreditation.StatusEnum.CONFIRMED);

        LocalDateTime expiredDateTime = LocalDateTime.now().minusDays(31);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        oldConfirmedAccreditation.setLastUpdate(expiredDateTime.format(formatter));
        addAccreditation(oldConfirmedAccreditation);

        //Expired
        Accreditation expiredAccreditation = new Accreditation("expired", Accreditation.AccreditationTypeEnum.BY_NET_WORTH, doc1);
        expiredAccreditation.setStatus(Accreditation.StatusEnum.EXPIRED);
        addAccreditation(expiredAccreditation);

        //Failed
        Accreditation failedAccreditation = new Accreditation("failed", Accreditation.AccreditationTypeEnum.BY_NET_WORTH, doc1);
        failedAccreditation.setStatus(Accreditation.StatusEnum.FAILED);
        addAccreditation(failedAccreditation);



        //UserA
        User userA =  new User("userA");
        userTable.put(userA.getId(), userA);

        addUserAccreditation(userA.getId(), pendingAccreditation.getId());
        addUserAccreditation(userA.getId(), expiredAccreditation.getId());

        //UserB
        User userB = new User("userB");
        userTable.put(userB.getId(), userB);

        addUserAccreditation(userB.getId(), confirmedAccreditation.getId());
        addUserAccreditation(userB.getId(), oldConfirmedAccreditation.getId());
        addUserAccreditation(userB.getId(), failedAccreditation.getId());

    }




    public void addAccreditation(Accreditation accreditation) {
        accreditationTable.put(accreditation.getId(), accreditation);
    }

    public Accreditation getAccreditation(String id) {
        //if doesn't exist return null
        if(!accreditationTable.containsKey(id)){
            return null;
        }

        //else return it
        return accreditationTable.get(id);
    }

    public List<Accreditation> getConfirmedAccreditations(){
        //simple filter to get ones which are confirmed
        return accreditationTable.values().stream().filter(accre -> accre.getStatus() == Accreditation.StatusEnum.CONFIRMED).collect(Collectors.toList());
    }

    public User getUser(String id){
        //check user if valid first
        if(id == null || id.isEmpty() || !userTable.containsKey(id)){
            return null;
        }

        return userTable.get(id);
    }

    public void addUserAccreditation(String userId, String accreditationId){
        //adds a connection between user and accreditation  to the db
        UserAccreditation newUA = new UserAccreditation(userId, accreditationId);
        userAccreditationsTable.add(newUA);

    }

    public List<String> getUserAccreditations(String userId) {

        //filter to get only the accreditation ids connected to the given user
        List<String> accreditationsList = userAccreditationsTable.stream().filter(ua -> ua.getUserId().equals(userId)).map(ua -> ua.getAccreditationId()).collect(Collectors.toList());

        return accreditationsList;
    }

    public boolean userHasPendingAccreditation(String userId) {

        //get all respective accreditations
        List<String> accreditationsIds = getUserAccreditations(userId);

        //Iterate to check if one is pending
        for(String accreditationId : accreditationsIds){
            Accreditation accreditation = getAccreditation(accreditationId);
            if(accreditation == null){
                continue;
            }

            if(accreditation.getStatus() == Accreditation.StatusEnum.PENDING){
                return true;
            }
        }

        return false;
    }

}
