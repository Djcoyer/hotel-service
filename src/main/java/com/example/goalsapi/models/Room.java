package com.example.goalsapi.models;

import lombok.Data;

@Data
public class Room {
    private String name;
    private double pricePerNight;
    private String details;
    private CompositeKey id;

    public Room(){}

    public Room(String roomName, double pricePerNight, String hotelId, int roomId, String details) {
        this.name = roomName;
        this.pricePerNight = pricePerNight;
        this.id = new CompositeKey(hotelId, roomId);
        this.details = details;
    }

    public Room(String roomName, double pricePerNight, CompositeKey roomId, String details) {
        this.name = roomName;
        this.pricePerNight = pricePerNight;
        this.id = roomId;
        this.details = details;
    }
}
