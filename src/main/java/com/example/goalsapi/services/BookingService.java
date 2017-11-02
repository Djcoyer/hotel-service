package com.example.goalsapi.services;

import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.dao.BookingDao;
import com.example.goalsapi.repositories.BookingRepository;
import com.example.goalsapi.transformers.BookingTransformer;
import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    Mongo mongo = new Mongo("localhost");
    private MongoTemplate mongoTemplate = new MongoTemplate(mongo, "Hotels");

    public ResponseEntity getBooking(String bookingId) {
        try{
            if(bookingId == null){
                return new ResponseEntity("Must supply a booking ID", HttpStatus.BAD_REQUEST);
            }
            BookingDao bookingDao = bookingRepository.findOne(bookingId);
            Booking booking = BookingTransformer.transform(bookingDao);
            return new ResponseEntity(booking, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addBooking(Booking booking) {
        try{
            if(booking == null) {
                return new ResponseEntity("Must supply a valid booking", HttpStatus.BAD_REQUEST);
            }
            booking.setBookingDates(getDateRange(booking.getBookingStartDate(), booking.getBookingEndDate()));
            List<LocalDate> bookingDates = booking.getBookingDates();
            CompositeKey roomId = booking.getRoomId();
            if(checkIfBookingExists(bookingDates, roomId)){
                return new ResponseEntity("Booking already exists on specified date.", HttpStatus.BAD_REQUEST);
            }
            else{
                booking.setBookingId(UUID.randomUUID().toString());
                BookingDao bookingDao = BookingTransformer.transform(booking);
                bookingRepository.insert(bookingDao);
                return new ResponseEntity("Booking successfully created. Id: " + bookingDao.getBookingId(), HttpStatus.OK);
            }
        }catch(Exception e){
            return new ResponseEntity("Something went wrong. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getBookings() {
        try{
            List<BookingDao> bookingDaos = bookingRepository.findAll();
            List<Booking> bookings = convertDaoList(bookingDaos);
            return new ResponseEntity(bookings, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean checkIfBookingExists(List<LocalDate> bookingDates, CompositeKey roomId) {

        Query query = new Query(Criteria.where("roomId").is(roomId));
        bookingDates.sort(Comparator.naturalOrder());
        int endIndex = bookingDates.size() -1;
        LocalDate from = bookingDates.get(0);
        LocalDate to = bookingDates.get(endIndex);
        query.addCriteria(Criteria.where("bookingDates").is(from)
                .andOperator(Criteria.where("bookingDates").is(to)));

        List<BookingDao> existingBookings = mongoTemplate.find(query, BookingDao.class);
        return (existingBookings != null && existingBookings.size() > 0);
    }

    private List<LocalDate> getDateRange(Date startDate, Date endDate){
        LocalDate ldStart  = new java.sql.Date(startDate.getTime()).toLocalDate();
        LocalDate ldEnd = new java.sql.Date(endDate.getTime()).toLocalDate();
        long numOfDaysBetween = ChronoUnit.DAYS.between(ldStart, ldEnd);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> ldStart.plusDays(i))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsByCustomerId(String customerId) {
        List<BookingDao> bookingDaos = bookingRepository.findAllByCustomerId(customerId);
        List<Booking> bookings = convertDaoList(bookingDaos);
        return bookings;
    }

    private List<Booking> convertDaoList(List<BookingDao> bookingDaos) {
        List<Booking> bookings = new ArrayList<>();
        for(BookingDao bookingDao : bookingDaos) {
            bookings.add(BookingTransformer.transform(bookingDao));
        }
        return bookings;
    }

    public ResponseEntity modifyBooking(String bookingId, Booking booking) {
        if(bookingId == null || booking == null) {
            return new ResponseEntity("Must provide valid booking information", HttpStatus.BAD_REQUEST);
        }
        booking.setBookingId(bookingId);
        if(booking.getBookingDates().size() < 1) {
            return new ResponseEntity("Must cancel booking if length is less than one day.", HttpStatus.BAD_REQUEST);
        }
        if(!bookingRepository.exists(bookingId)){
            return new ResponseEntity("Invalid booking ID", HttpStatus.BAD_REQUEST);
        }
        BookingDao bookingDao = BookingTransformer.transform(booking);
        bookingRepository.save(bookingDao);
        return new ResponseEntity(bookingDao, HttpStatus.OK);
    }
}
