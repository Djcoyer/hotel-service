package com.example.goalsapi.repositories;

import com.example.goalsapi.models.dao.CustomerDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository <CustomerDao, String>{

    @Query(value= "{_id:?0}", fields = "{refreshToken: 1}")
    CustomerDao getRefreshToken(String customerId);

}
