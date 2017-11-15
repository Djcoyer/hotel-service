package com.example.goalsapi.repositories;

import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.dao.BookingDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.awt.*;
import java.util.List;

public interface BookingRepository extends MongoRepository<BookingDao, String>{

    List<BookingDao> findAllByCustomerId(String customerId);

    @Query(value = "{roomId.hotelId: ?0, roomId.roomNumber:{$exists: true}}")
    List<BookingDao> findAllByHotelId(String hotelId);

    List<BookingDao> findAllByRoomId(CompositeKey roomId);

    @Query(fields = "{bookingDates:1}")
    List<BookingDao> findAllDatesByRoomId(CompositeKey roomId);
}
