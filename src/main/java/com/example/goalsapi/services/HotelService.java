package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.InternalServerException;
import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.Exceptions.NotFoundException;
import com.example.goalsapi.models.Hotel;
import com.example.goalsapi.models.dao.HotelDao;
import com.example.goalsapi.repositories.HotelRepository;
import com.example.goalsapi.transformers.HotelTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel getHotel(String hotelId) {
        if (hotelId == null) throw new InvalidInputException();
        HotelDao hotelDao = hotelRepository.findOne(hotelId);
        if (hotelDao != null) {
            Hotel hotel = HotelTransformer.transform(hotelDao);
            return hotel;
        } else {
            throw new NotFoundException();
        }
    }

    public Hotel addHotel(Hotel hotel) {
            if (hotel == null || hotel.getName() == null) throw new InvalidInputException();
            HotelDao hotelDao = HotelTransformer.transform(hotel);
            hotelRepository.insert(hotelDao);
            return hotel;
        }

    public List<Hotel> getAllHotels() {
            List<HotelDao> hotelDaos = hotelRepository.findAll();
            List<Hotel> hotels = new ArrayList<>();
            for (HotelDao hotelDao : hotelDaos) {
                hotels.add(HotelTransformer.transform(hotelDao));
            }
            return hotels;
    }

    public String getHotelNameFromId(String hotelId){
        if(hotelId == null) throw new InvalidInputException();
        HotelDao hotelDao = hotelRepository.findHotelNameById(hotelId);
        String name = hotelDao.getName();
        if(name == null) throw new NotFoundException();
        return name;
    }

    public boolean hotelExists(String hotelId) {
        return hotelRepository.exists(hotelId);
    }
}
