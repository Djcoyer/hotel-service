package com.example.goalsapi.models;

import lombok.Data;

@Data
public class CustomInfo {
    private String firstName;
    private String lastName;
    private String uid;

    public CustomInfo(){}

    public CustomInfo(String firstName, String lastName, String uid){
        this.firstName = firstName;
        this.lastName =lastName;
        this.uid = uid;
    }
}
