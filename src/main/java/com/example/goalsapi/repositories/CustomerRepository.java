package com.example.goalsapi.repositories;

import com.example.goalsapi.models.dao.CustomerDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository <CustomerDao, String>{

}
