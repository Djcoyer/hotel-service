package com.example.goalsapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Booking {
    private String id;
    private String customerId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date bookingStartDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date bookingEndDate;
    private List<Date> bookingDates;
    private CompositeKey roomId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String roomName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String hotelName;

    public Booking(){}

    public Booking(String customerId, Date bookingStartDate, Date bookingEndDate, String hotelId, int roomNumber) {
        this.customerId = customerId;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.roomId = new CompositeKey(hotelId, roomNumber);
        this.id = UUID.randomUUID().toString();
    }

    public Booking(String id, String customerId, List<Date> bookingDates, CompositeKey roomId) {
        this.customerId = customerId;
        this.bookingDates = bookingDates;
        this.roomId = roomId;
        this.id = id;
    }

}
