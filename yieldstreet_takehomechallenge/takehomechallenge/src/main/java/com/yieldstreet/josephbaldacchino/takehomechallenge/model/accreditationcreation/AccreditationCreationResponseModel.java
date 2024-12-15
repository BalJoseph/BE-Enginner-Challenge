package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;

public class AccreditationCreationResponseModel {
    private String accreditation_id;

    public AccreditationCreationResponseModel(Accreditation accreditation){
        accreditation_id = accreditation.getId();
    }

    public String getAccreditation_id() {
        return accreditation_id;
    }
}
