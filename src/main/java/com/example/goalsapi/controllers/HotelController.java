package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Hotel;
import com.example.goalsapi.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Hotel> getAllHotels(){
        return hotelService.getAllHotels();
    }

    @GetMapping("/{hotelId}")
    @ResponseStatus(HttpStatus.OK)
    public Hotel getHotel(@PathVariable String hotelId) {
        return hotelService.getHotel(hotelId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Hotel addHotel(@RequestBody Hotel hotel) {
        return hotelService.addHotel(hotel);
    }

}
