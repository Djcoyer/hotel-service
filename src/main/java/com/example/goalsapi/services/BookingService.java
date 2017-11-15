package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.ConflictException;
import com.example.goalsapi.Exceptions.ForbiddenException;
import com.example.goalsapi.Exceptions.InternalServerException;
import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.dao.BookingDao;
import com.example.goalsapi.repositories.BookingRepository;
import com.example.goalsapi.transformers.BookingTransformer;
import com.mongodb.Mongo;
import com.monitorjbl.json.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.monitorjbl.json.Match.match;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private AuthService authService;
    private RoomService roomService;

    Mongo mongo = new Mongo("localhost");
    private MongoTemplate mongoTemplate = new MongoTemplate(mongo, "Hotels");

    @Autowired
    public BookingService(BookingRepository bookingRepository, AuthService authService, RoomService roomService){
        this.bookingRepository = bookingRepository;
        this.authService = authService;
        this.roomService = roomService;
    }

    //region Get Bookings

    public ResponseEntity getBooking(String bookingId) {
        try {
            if (bookingId == null) {
                return new ResponseEntity("Must supply a booking ID", HttpStatus.BAD_REQUEST);
            }
            BookingDao bookingDao = bookingRepository.findOne(bookingId);
            Booking booking = BookingTransformer.transform(bookingDao);
            return new ResponseEntity(booking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getBookingsByHotelId(String hotelId, HotelService hotelService) {
        if (hotelId == null) {
            return new ResponseEntity("Must supply hotel ID", HttpStatus.BAD_REQUEST);
        }
        if (!hotelService.hotelExists(hotelId)) {
            return new ResponseEntity("Must supply a valid hotel ID. Hotel does not exist", HttpStatus.BAD_REQUEST);
        }
        List<BookingDao> bookingDaos = bookingRepository.findAllByHotelId(hotelId);
        List<Booking> bookings = convertDaoList(bookingDaos);
        return new ResponseEntity(bookings, HttpStatus.OK);
    }

    public List<Booking> getCustomerBookings(String authorization) {
        String customerId = authService.getCustomerIdFromAuthorizationHeader(authorization);
        List<BookingDao> bookingDaos = bookingRepository.findAllByCustomerId(customerId);
        List<Booking> bookings = convertDaoList(bookingDaos);
        for(Booking booking: bookings){
            booking.setRoomName(roomService.getRoomName(booking.getRoomId()));
        }
        return bookings;
    }

    public List<Booking> getBookings() {
        try {
            List<BookingDao> bookingDaos = bookingRepository.findAll();
            List<Booking> bookings = convertDaoList(bookingDaos);
            JsonView.with(bookings).onClass(Booking.class, match().exclude("bookingStartDate", "bookingEndDate"));
            return bookings;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    public List<Booking> getBookingsByRoomId(String hotelId, int roomNum, RoomService roomService) {
        CompositeKey roomId = new CompositeKey(hotelId, roomNum);
        if (!roomService.roomExists(roomId)) {
            throw new InvalidInputException();
        }
        List<BookingDao> bookingDaos = bookingRepository.findAllDatesByRoomId(roomId);
        List<Booking> bookings = convertDaoList(bookingDaos);
        return bookings;
    }

    //endregion

    //region Add Bookings

    public Booking addBooking(Booking booking, String authorization) {
        if (booking == null) {
            throw new InvalidInputException();
        }
        List<LocalDate> localDates = getDateRange(booking.getBookingStartDate(), booking.getBookingEndDate());
        List<Date> dates = new ArrayList<>();
        for (LocalDate localDate : localDates) {
            dates.add(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        booking.setBookingDates(dates);
        List<Date> bookingDates = booking.getBookingDates();
        CompositeKey roomId = booking.getRoomId();
        if (checkIfBookingExists(bookingDates, roomId)) {
            throw new ConflictException();
        } else {
            String customerId = authService.getCustomerIdFromAuthorizationHeader(authorization);
            booking.setCustomerId(customerId);
            booking.setId(UUID.randomUUID().toString());
            BookingDao bookingDao = BookingTransformer.transform(booking);
            bookingRepository.insert(bookingDao);
            return booking;
        }
    }

    //endregion

    //region Patch Bookings

    public ResponseEntity modifyBooking(String bookingId, Booking booking) {
        if (bookingId == null || booking == null) {
            return new ResponseEntity("Must provide valid booking information", HttpStatus.BAD_REQUEST);
        }
        booking.setId(bookingId);
        if (booking.getBookingDates().size() < 1) {
            return new ResponseEntity("Must cancel booking if length is less than one day.", HttpStatus.BAD_REQUEST);
        }
        if (!bookingRepository.exists(bookingId)) {
            return new ResponseEntity("Invalid booking ID", HttpStatus.BAD_REQUEST);
        }
        BookingDao bookingDao = BookingTransformer.transform(booking);
        bookingRepository.save(bookingDao);
        return new ResponseEntity(bookingDao, HttpStatus.OK);
    }

    //endregion

    //region Helpers

    private boolean checkIfBookingExists(List<Date> bookingDates, CompositeKey roomId) {

        Query query = new Query(Criteria.where("roomId").is(roomId));
        int endIndex = bookingDates.size() - 1;
        Date from = bookingDates.get(0);
        Date to = bookingDates.get(endIndex);
        System.out.println(from + " - " + to);
        query.addCriteria(Criteria.where("bookingDates").in(bookingDates));

        List<BookingDao> existingBookings = mongoTemplate.find(query, BookingDao.class);
        return (existingBookings != null && existingBookings.size() > 0);
    }

    private List<LocalDate> getDateRange(Date startDate, Date endDate) {
        LocalDate ldStart = new java.sql.Date(startDate.getTime()).toLocalDate().plusDays(1);
        LocalDate ldEnd = new java.sql.Date(endDate.getTime()).toLocalDate().plusDays(2);
        long numOfDaysBetween = ChronoUnit.DAYS.between(ldStart, ldEnd);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> ldStart.plusDays(i))
                .collect(Collectors.toList());
    }

    private List<Booking> convertDaoList(List<BookingDao> bookingDaos) {
        List<Booking> bookings = new ArrayList<>();
        for (BookingDao bookingDao : bookingDaos) {
            bookings.add(BookingTransformer.transform(bookingDao));
        }
        return bookings;
    }

    //endregion

}
