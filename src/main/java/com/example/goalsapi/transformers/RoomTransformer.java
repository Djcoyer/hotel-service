package com.example.goalsapi.transformers;

import com.example.goalsapi.models.Room;
import com.example.goalsapi.models.dao.RoomDao;

public class RoomTransformer {

    public static Room transform(RoomDao roomDao) {
        try{
            Room room = new Room();
            room.setPricePerNight(roomDao.getPricePerNight());
            room.setId(roomDao.getId());
            room.setDetails(roomDao.getDetails());
            room.setName(roomDao.getName());
            return room;
        }catch(Exception e) {
            throw e;
        }
    }

    public static RoomDao transform(Room room) {
        try{
            RoomDao roomDao = new RoomDao();
            roomDao.setPricePerNight(room.getPricePerNight());
            roomDao.setId(room.getId());
            roomDao.setDetails(room.getDetails());
            roomDao.setName(room.getName());
            return roomDao;
        }catch(Exception e){
            throw e;
        }
    }
}
