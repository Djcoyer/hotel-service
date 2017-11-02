package com.example.goalsapi.repositories;

import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.dao.RoomDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<RoomDao, CompositeKey>{
}
