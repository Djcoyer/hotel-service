package com.example.goalsapi.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompositeKey implements Serializable {
    private String hotelId;
    private int roomNumber;

    public CompositeKey(){}

    public CompositeKey(String hotelId, int  roomNumber) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
    }
}
