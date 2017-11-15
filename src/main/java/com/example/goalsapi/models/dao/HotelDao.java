package com.example.goalsapi.models.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "hotels")
public class HotelDao {
    @Id
    private String id;
    private String address;
    private String name;
    private String city;
    private String state;
    private String description;
    private int zipCode;
    private double rating;
    public HotelDao(){}

    public HotelDao(String address, String name, String city, String state, String description, int zipCode, double rating){
        this.id = UUID.randomUUID().toString();
        this.address = address;
        this.name = name;
        this.rating = rating;
        this.city = city;
        this.state = state;
        this.description = description;
        this.zipCode = zipCode;
    }

    public HotelDao(String id, String address, String name, String city, String state, String description, int zipCode, long rating){
        this.id = id;
        this.address = address;
        this.name = name;
        this.rating = rating;
        this.city = city;
        this.description = description;
        this.zipCode = zipCode;
        this.state = state;
    }

}
