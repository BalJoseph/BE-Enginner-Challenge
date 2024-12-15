package com.yieldstreet.josephbaldacchino.takehomechallenge.model.database;

import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.DocumentCreationModel;

public class Document {

    private String name;
    private String mimeType;
    private String content;

    public Document(String name, String mimeType, String content) {
        this.name = name;
        this.mimeType = mimeType;
        this.content = content;
    }

    public Document(DocumentCreationModel dcm){
        name = dcm.getName();
        mimeType = dcm.getMime_type();
        content = dcm.getContent();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

