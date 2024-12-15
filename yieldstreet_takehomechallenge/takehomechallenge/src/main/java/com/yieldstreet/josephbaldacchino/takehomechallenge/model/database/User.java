package com.yieldstreet.josephbaldacchino.takehomechallenge.model.database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String id;

    public User(){
        id = UUID.randomUUID().toString();
    }

    public User(String user_id){
        id = user_id;
    }

    public String getId() {
        return id;
    }

}
