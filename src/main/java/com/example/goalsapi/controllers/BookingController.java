package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Booking;
import com.example.goalsapi.services.BookingService;
import com.example.goalsapi.services.HotelService;
import com.example.goalsapi.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bookings")
public class BookingController {


    //region SETUP

    private BookingService bookingService;

    private HotelService hotelService;

    private RoomService roomService;

    @Autowired
    public BookingController(BookingService bookingService,
                             HotelService hotelService,
                             RoomService roomService) {
        this.bookingService = bookingService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    //endregion

    //region GET
    @GetMapping("")
    public List<Booking> getBookings(){
        return bookingService.getBookings();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity getBooking(@PathVariable String bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity getHotelBookings(@PathVariable String hotelId){
        return bookingService.getBookingsByHotelId(hotelId, hotelService);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{hotelId}/{roomNum}")
    public List<Booking> getRoomBookings(@PathVariable String hotelId, @PathVariable int roomNum){
        return bookingService.getBookingsByRoomId(hotelId, roomNum, roomService);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/customer")
    public List<Booking> getCustomerBookings(@RequestHeader String authorization){
        return bookingService.getCustomerBookings(authorization);
    }


    //endregion

    //region POST

    @PostMapping("")
    public Booking addBooking(@RequestBody Booking booking, @RequestHeader String authorization) {
        return bookingService.addBooking(booking, authorization);
    }

    //endregion

    //region PATCH

    @PatchMapping("/{bookingId}")
    public ResponseEntity modifyBooking(@PathVariable  String bookingId, @RequestBody Booking booking) {
        return bookingService.modifyBooking(bookingId, booking);
    }

    //endregion
}
