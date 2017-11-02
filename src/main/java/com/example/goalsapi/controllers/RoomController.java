package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Room;
import com.example.goalsapi.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public ResponseEntity getRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{hotelId}/{roomId}")
    public ResponseEntity getRoom(@PathVariable String hotelId, @PathVariable int roomId) {
        return roomService.getRoom(hotelId, roomId);
    }

    @PostMapping("")
    public ResponseEntity createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
    }

}
