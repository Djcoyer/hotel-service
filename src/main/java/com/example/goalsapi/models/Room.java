package com.example.goalsapi.models;

import lombok.Data;

@Data
public class Room {
    private String roomName;
    private double pricePerNight;
    private CompositeKey roomId;

    public Room(){}

    public Room(String roomName, double pricePerNight, String hotelId, int roomId) {
        this.roomName = roomName;
        this.pricePerNight = pricePerNight;
        this.roomId = new CompositeKey(hotelId, roomId);
    }

    public Room(String roomName, double pricePerNight, CompositeKey roomId) {
        this.roomName = roomName;
        this.pricePerNight = pricePerNight;
        this.roomId = roomId;
    }
}
