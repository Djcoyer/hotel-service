package com.example.goalsapi.services;

import com.example.goalsapi.models.Hotel;
import com.example.goalsapi.models.dao.HotelDao;
import com.example.goalsapi.repositories.HotelRepository;
import com.example.goalsapi.transformers.HotelTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public ResponseEntity getHotel(String hotelId) {
        try{
            HotelDao hotelDao = hotelRepository.findOne(hotelId);
            if(hotelDao != null){
                Hotel hotel = HotelTransformer.transform(hotelDao);
                return new ResponseEntity(hotel, HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Must submit valid hotelId", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addHotel(Hotel hotel) {
        try {
            if (hotel == null) {
                return new ResponseEntity("Must include valid hotel object", HttpStatus.BAD_REQUEST);
            }
            hotel.setHotelId(UUID.randomUUID().toString());
            HotelDao hotelDao = HotelTransformer.transform(hotel);
            hotelRepository.insert(hotelDao);
            return new ResponseEntity("Hotel successfully saved. ID: " + hotelDao.getHotelId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getAllHotels() {
        try{
            List<HotelDao> hotelDaos = hotelRepository.findAll();
            List<Hotel> hotels = new ArrayList<>();
            for(HotelDao hotelDao : hotelDaos) {
                hotels.add(HotelTransformer.transform(hotelDao));
            }
            return new ResponseEntity(hotels, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }
}
