package com.example.goalsapi.transformers;

import com.example.goalsapi.models.Room;
import com.example.goalsapi.models.dao.RoomDao;

public class RoomTransformer {

    public static Room transform(RoomDao roomDao) {
        try{
            Room room = new Room();
            room.setPricePerNight(roomDao.getPricePerNight());
            room.setRoomId(roomDao.getRoomId());
            room.setRoomName(roomDao.getRoomName());
            return room;
        }catch(Exception e) {
            throw e;
        }
    }

    public static RoomDao transform(Room room) {
        try{
            RoomDao roomDao = new RoomDao();
            roomDao.setPricePerNight(room.getPricePerNight());
            roomDao.setRoomId(room.getRoomId());
            roomDao.setRoomName(room.getRoomName());
            return roomDao;
        }catch(Exception e){
            throw e;
        }
    }
}
