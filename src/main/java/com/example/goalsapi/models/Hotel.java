package com.example.goalsapi.models;

import lombok.Data;

@Data
public class Hotel {
    private String id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String description;
    private int zipCode;
    private double rating;

    public Hotel(){}

    public Hotel(String id, String name, String address, String city, String state, String description, int zipCode, double rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.city = city;
        this.state = state;
        this.description = description;
        this.zipCode = zipCode;
    }
}
