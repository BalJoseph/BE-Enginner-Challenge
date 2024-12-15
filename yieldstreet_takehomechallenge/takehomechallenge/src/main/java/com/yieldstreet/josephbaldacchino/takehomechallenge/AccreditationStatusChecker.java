package com.yieldstreet.josephbaldacchino.takehomechallenge;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;
import com.yieldstreet.josephbaldacchino.takehomechallenge.service.DatabaseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AccreditationStatusChecker {

    private final DatabaseService dbService;

    public AccreditationStatusChecker(DatabaseService dbService){
        this.dbService = dbService;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) //24 hours -> 60 minutes -> 60 seconds -> 1000 ms
    //@Scheduled(fixedRate = 1000) //This is just for debugging purposes
    public void checkForExpired(){

        System.out.println("Checking for expired");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        //Get all which are confirmed
        List<Accreditation> confirmedAccreditations = dbService.getConfirmedAccreditations();
        //Iterate through
        for(Accreditation accreditation : confirmedAccreditations){

            LocalDateTime lastUpdateLDT = LocalDateTime.parse(accreditation.getLastUpdate(), formatter);

            //Get the time between last update and now
            Duration interval = Duration.between(lastUpdateLDT, now);
            long daysInterval = interval.toDays();

            //If greater than 30 days then it has to be set as expired
            if(daysInterval > 30){
                accreditation.setStatus(Accreditation.StatusEnum.EXPIRED);
                accreditation.setLastUpdate(now.format(formatter));
            }
        }
    }


}
