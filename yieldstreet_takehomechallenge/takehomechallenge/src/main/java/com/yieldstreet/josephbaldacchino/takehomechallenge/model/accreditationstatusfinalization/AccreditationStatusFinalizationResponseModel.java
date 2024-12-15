package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationstatusfinalization;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;

public class AccreditationStatusFinalizationResponseModel {
    private String accreditation_id;

    public AccreditationStatusFinalizationResponseModel(Accreditation accreditation) {
        accreditation_id = accreditation.getId();
    }

    public String getAccreditation_id() {
        return accreditation_id;
    }
}
