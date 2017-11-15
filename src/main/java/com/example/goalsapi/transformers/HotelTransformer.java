package com.example.goalsapi.transformers;

import com.example.goalsapi.models.Hotel;
import com.example.goalsapi.models.dao.HotelDao;

public class HotelTransformer {

    public static Hotel transform(HotelDao hotelDao) {
        Hotel hotel = new Hotel();
        try{
            hotel.setAddress(hotelDao.getAddress());
            hotel.setCity(hotelDao.getCity());
            hotel.setState(hotelDao.getState());
            hotel.setZipCode(hotelDao.getZipCode());
            hotel.setId(hotelDao.getId());
            hotel.setDescription(hotelDao.getDescription());
            hotel.setName(hotelDao.getName());
            hotel.setRating(hotelDao.getRating());
            return hotel;
        }
        catch(Exception e) {
            throw e;
        }
    }

    public static HotelDao transform(Hotel hotel) {
        HotelDao hotelDao = new HotelDao();
        try{
            hotelDao.setAddress(hotel.getAddress());
            hotelDao.setCity(hotel.getCity());
            hotelDao.setState(hotel.getState());
            hotelDao.setZipCode(hotel.getZipCode());
            hotelDao.setId(hotel.getId());
            hotelDao.setDescription(hotel.getDescription());
            hotelDao.setName(hotel.getName());
            hotelDao.setRating(hotel.getRating());
            return hotelDao;
        }
        catch(Exception e) {
            throw e;
        }
    }
}
