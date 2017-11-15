package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Hotel;
import com.example.goalsapi.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("")
    public ResponseEntity getAllHotels(){
        return hotelService.getAllHotels();
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity getHotel(@PathVariable String hotelId) {
        return hotelService.getHotel(hotelId);
    }

    @PostMapping("")
    public ResponseEntity addHotel(@RequestBody Hotel hotel) {
        hotel.setId(hotelService.getUUID());
        return hotelService.addHotel(hotel);
    }

}
