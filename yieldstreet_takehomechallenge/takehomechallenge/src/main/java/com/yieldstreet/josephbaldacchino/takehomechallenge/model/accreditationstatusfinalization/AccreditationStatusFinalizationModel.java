package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationstatusfinalization;

import jakarta.validation.constraints.Pattern;

public class AccreditationStatusFinalizationModel {

    @Pattern(regexp = "^(CONFIRMED|EXPIRED|FAILED)$", message = "Only 'CONFIRMED', 'EXPIRED' or 'FAILED' are allowed.")
    private String outcome;

    public AccreditationStatusFinalizationModel(String outcome){
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }
}
