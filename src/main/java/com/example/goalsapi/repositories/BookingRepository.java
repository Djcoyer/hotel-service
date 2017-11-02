package com.example.goalsapi.repositories;

import com.example.goalsapi.models.dao.BookingDao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<BookingDao, String>{

    List<BookingDao> findAllByCustomerId(String customerId);

}
