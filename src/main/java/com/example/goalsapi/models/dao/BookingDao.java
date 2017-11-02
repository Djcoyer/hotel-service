package com.example.goalsapi.models.dao;

import com.example.goalsapi.models.CompositeKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Document(collection = "bookings")
public class BookingDao {
    @Id
    private String bookingId;
    private String customerId;
    private CompositeKey roomId;
    private List<LocalDate> bookingDates;

    public BookingDao(){}

    public BookingDao(String customerId, String hotelId, int roomId, List<LocalDate> bookingDates) {
        this.customerId = customerId;
        this.roomId = new CompositeKey(hotelId, roomId);
        this.bookingDates = bookingDates;
        this.bookingId = UUID.randomUUID().toString();
    }

    public BookingDao(String bookingId, String customerId, String hotelId, int roomId, List<LocalDate> bookingDates) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.roomId = new CompositeKey(hotelId, roomId);
        this.bookingDates = bookingDates;
    }
}
