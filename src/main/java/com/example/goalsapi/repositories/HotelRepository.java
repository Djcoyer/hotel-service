package com.example.goalsapi.repositories;

import com.example.goalsapi.models.dao.HotelDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface HotelRepository extends MongoRepository<HotelDao, String>{

    @Query(value = "{_id:?0}", fields = "{name: 1}")
    HotelDao findHotelNameById(String hotelId);
}
