package com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation;

import jakarta.validation.constraints.NotEmpty;

public class DocumentCreationModel {
    @NotEmpty
    private String name;
    @NotEmpty
    private String mime_type;
    @NotEmpty
    private String content;

    public DocumentCreationModel(String name, String mime_type, String content) {
        this.name = name;
        this.mime_type = mime_type;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getMime_type() {
        return mime_type;
    }

    public String getContent() {
        return content;
    }

}
