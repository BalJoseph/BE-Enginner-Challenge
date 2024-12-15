package com.yieldstreet.josephbaldacchino.takehomechallenge.model.database;

import java.util.UUID;

public class UserAccreditation {
    private String id;
    private String userId;
    private String accreditationId;

    public UserAccreditation(String userId, String accreditationId) {
        id = UUID.randomUUID().toString();
        this.userId = userId;
        this.accreditationId = accreditationId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccreditationId() {
        return accreditationId;
    }
}
