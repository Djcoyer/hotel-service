package com.example.goalsapi.transformers;

import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.dao.BookingDao;

public class BookingTransformer {

    public static Booking transform(BookingDao bookingDao) {
        try{
            Booking booking = new Booking();
            booking.setBookingId(bookingDao.getBookingId());
            booking.setBookingDates(bookingDao.getBookingDates());
            booking.setCustomerId(bookingDao.getCustomerId());
            booking.setRoomId(bookingDao.getRoomId());
            return booking;
        } catch(Exception e){
            throw e;
        }
    }

    public static BookingDao transform(Booking booking) {
        try{
            BookingDao bookingDao = new BookingDao();
            bookingDao.setBookingId(booking.getBookingId());
            bookingDao.setBookingDates(booking.getBookingDates());
            bookingDao.setCustomerId(booking.getCustomerId());
            bookingDao.setRoomId(booking.getRoomId());
            return bookingDao;
        } catch(Exception e){
            throw e;
        }
    }
}
