package com.example.goalsapi.models.dao;

import com.example.goalsapi.models.CompositeKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rooms")
public class RoomDao {
    @Id
    private CompositeKey roomId;
    public String roomName;
    private double pricePerNight;

    public RoomDao(){}

    public RoomDao(String hotelId, int roomId, String roomName, double pricePerNight) {
        this.roomId = new CompositeKey(hotelId, roomId);
        this.roomName = roomName;
        this.pricePerNight = pricePerNight;
    }

    public RoomDao(CompositeKey roomId, String roomName, double pricePerNight) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.pricePerNight = pricePerNight;
    }

}
