package com.android.bakingapp.models;

public class Step {

    public String id;
    public String shortDescription;
    public String description;
    public String videoURL;

    public Step(String id, String shortDescription, String description, String videoURL){
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
    }
}
