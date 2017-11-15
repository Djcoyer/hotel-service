package com.example.goalsapi.repositories;

import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.Room;
import com.example.goalsapi.models.dao.RoomDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoomRepository extends MongoRepository<RoomDao, CompositeKey>{

    @Query(value = "{_id.hotelId: ?0, _id.roomNumber:{$exists: true}}")
    List<RoomDao> findAllByHotelId(String hotelId);

    @Query(value = "{_id: ?0}", fields = "{name: 1}")
    RoomDao getRoomNameById(CompositeKey roomId);
}
