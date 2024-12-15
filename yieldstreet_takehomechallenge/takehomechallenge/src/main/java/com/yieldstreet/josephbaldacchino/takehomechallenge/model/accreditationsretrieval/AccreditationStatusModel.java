package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationsretrieval;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Document;

public class AccreditationStatusModel {
    private Accreditation.AccreditationTypeEnum accreditation_type;
    private Accreditation.StatusEnum status;

    public AccreditationStatusModel(){}
    public AccreditationStatusModel(Accreditation accreditation) {
        accreditation_type = accreditation.getAccreditationType();
        status = accreditation.getStatus();
    }

    public Accreditation.AccreditationTypeEnum getAccreditation_type() {
        return accreditation_type;
    }

    public Accreditation.StatusEnum getStatus() {
        return status;
    }
}
