package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationsretrieval;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAccreditationsResponseModel {

    private String user_id;
    private Map<String, AccreditationStatusModel> accreditation_statuses;

    public UserAccreditationsResponseModel(){}

    public UserAccreditationsResponseModel(String userId, List<Accreditation> accreditations) {
        this.user_id = userId;

        accreditation_statuses = new HashMap<>();

        for(Accreditation acc : accreditations){
            AccreditationStatusModel asm = new AccreditationStatusModel(acc);
            accreditation_statuses.put(acc.getId(), asm);
        }

    }

    public String getUser_id() {
        return user_id;
    }

    public Map<String, AccreditationStatusModel> getAccreditation_statuses() {
        return accreditation_statuses;
    }
}
