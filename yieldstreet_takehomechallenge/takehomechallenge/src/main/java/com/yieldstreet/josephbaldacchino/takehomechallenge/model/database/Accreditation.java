package com.yieldstreet.josephbaldacchino.takehomechallenge.model.database;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.AccreditationCreationModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Accreditation {
    public enum AccreditationTypeEnum{
        BY_INCOME,
        BY_NET_WORTH
    }

    public enum StatusEnum{
        PENDING,
        CONFIRMED,
        EXPIRED,
        FAILED
    }

    private String id;
    private AccreditationTypeEnum accreditationType;

    private StatusEnum status;

    private Document document;

    private String lastUpdate;

    public Accreditation(String id, AccreditationTypeEnum accreditationType, Document document){
        this.id = id;
        this.accreditationType = accreditationType;
        status = StatusEnum.PENDING;
        this.document = document;

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        lastUpdate = currentDateTime.format(formatter);

    }

    public Accreditation(AccreditationTypeEnum accreditationType, Document document){
        id = UUID.randomUUID().toString();
        this.accreditationType = accreditationType;
        status = StatusEnum.PENDING;
        this.document = document;

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        lastUpdate = currentDateTime.format(formatter);
    }

    public Accreditation(AccreditationCreationModel acm){
        id = UUID.randomUUID().toString();
        status = StatusEnum.PENDING;
        document = new Document(acm.getDocument());

        setAccreditationType(AccreditationTypeEnum.valueOf(acm.getAccreditation_type()));

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        lastUpdate = currentDateTime.format(formatter);

    }

    public String getId() {
        return id;
    }

    public AccreditationTypeEnum getAccreditationType() {
        return accreditationType;
    }

    public void setAccreditationType(AccreditationTypeEnum accreditationType) {
        this.accreditationType = accreditationType;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
