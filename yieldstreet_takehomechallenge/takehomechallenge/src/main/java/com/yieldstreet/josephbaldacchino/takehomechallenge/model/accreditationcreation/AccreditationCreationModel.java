package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AccreditationCreationModel {
    @NotEmpty
    private String user_id;
    @Pattern(regexp = "^(BY_INCOME|BY_NET_WORTH)$", message = "Only 'BY_INCOME' or 'BY_NET_WORTH' allowed.")
    private String accreditation_type;

    @Valid @NotNull
    private DocumentCreationModel document;

    public AccreditationCreationModel(String user_id, String accreditation_type, DocumentCreationModel document) {
        this.user_id = user_id;
        this.accreditation_type = accreditation_type;
        this.document = document;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAccreditation_type() {
        return accreditation_type;
    }

    public DocumentCreationModel getDocument() {
        return document;
    }
}
