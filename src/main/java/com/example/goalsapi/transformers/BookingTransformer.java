package com.example.goalsapi.transformers;

import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.dao.BookingDao;

public class BookingTransformer {

    public static Booking transform(BookingDao bookingDao) {
        try{
            Booking booking = new Booking();
            booking.setId(bookingDao.getId());
            booking.setBookingDates(bookingDao.getBookingDates());
            booking.setCustomerId(bookingDao.getCustomerId());
            booking.setRoomId(bookingDao.getRoomId());
            booking.setBookingStartDate(bookingDao.getBookingStartDate());
            booking.setBookingEndDate(bookingDao.getBookingEndDate());
            return booking;
        } catch(Exception e){
            throw e;
        }
    }

    public static BookingDao transform(Booking booking) {
        try{
            BookingDao bookingDao = new BookingDao();
            bookingDao.setId(booking.getId());
            bookingDao.setBookingDates(booking.getBookingDates());
            bookingDao.setCustomerId(booking.getCustomerId());
            bookingDao.setRoomId(booking.getRoomId());
            bookingDao.setBookingStartDate(booking.getBookingStartDate());
            bookingDao.setBookingEndDate(booking.getBookingEndDate());
            return bookingDao;
        } catch(Exception e){
            throw e;
        }
    }
}
