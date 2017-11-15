package com.example.goalsapi.models.dao;

import com.example.goalsapi.models.CompositeKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rooms")
public class RoomDao {
    @Id
    private CompositeKey id;
    public String name;
    private double pricePerNight;
    private String details;

    public RoomDao(){}

    public RoomDao(String hotelId, int roomId, String roomName, double pricePerNight, String details) {
        this.id = new CompositeKey(hotelId, roomId);
        this.name = roomName;
        this.pricePerNight = pricePerNight;
        this.details = details;
    }

    public RoomDao(CompositeKey roomId, String roomName, double pricePerNight, String details) {
        this.id= roomId;
        this.name = roomName;
        this.pricePerNight = pricePerNight;
        this.details = details;
    }

}
