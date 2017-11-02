package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Booking;
import com.example.goalsapi.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("")
    public ResponseEntity getBookings(){
        return bookingService.getBookings();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity getBooking(@PathVariable String bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @PostMapping("")
    public ResponseEntity addBooking(@RequestBody Booking booking) {
        return bookingService.addBooking(booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity modifyBooking(@PathVariable  String bookingId, @RequestBody Booking booking) {
        return bookingService.modifyBooking(bookingId, booking);
    }
}
