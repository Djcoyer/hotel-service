package com.example.goalsapi.repositories;

import com.example.goalsapi.models.dao.HotelDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelRepository extends MongoRepository<HotelDao, String>{

}
