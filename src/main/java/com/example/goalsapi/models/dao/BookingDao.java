package com.example.goalsapi.models.dao;

import com.example.goalsapi.models.CompositeKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    private String id;
    private String customerId;
    private CompositeKey roomId;
    private Date bookingStartDate;
    private Date bookingEndDate;
    private List<Date> bookingDates;

    public BookingDao(){}

    public BookingDao(String customerId, String hotelId, int roomNumber, List<Date> bookingDates, Date bookingStartDate, Date bookingEndDate) {
        this.customerId = customerId;
        this.roomId = new CompositeKey(hotelId, roomNumber);
        this.bookingDates = bookingDates;
        this.id = UUID.randomUUID().toString();
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
    }

    public BookingDao(String id, String customerId, String hotelId, int roomNumber, List<Date> bookingDates, Date bookingStartDate, Date bookingEndDate) {
        this.id = id;
        this.customerId = customerId;
        this.roomId = new CompositeKey(hotelId, roomNumber);
        this.bookingDates = bookingDates;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
    }
}
