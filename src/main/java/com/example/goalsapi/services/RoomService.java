package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.NotFoundException;
import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.Room;
import com.example.goalsapi.models.dao.RoomDao;
import com.example.goalsapi.repositories.RoomRepository;
import com.example.goalsapi.transformers.RoomTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    public RoomService(RoomRepository roomRepository, HotelService hotelService){
        this.roomRepository = roomRepository;
        this.hotelService = hotelService;
    }

    public ResponseEntity getAllRooms(String hotelId) {
        try {
            List<RoomDao> roomDaos = roomRepository.findAllByHotelId(hotelId);
            if(roomDaos == null || roomDaos.size() == 0){
                return new ResponseEntity("Must provide a valid hotel ID",HttpStatus.BAD_REQUEST);
            }
            List<Room> rooms = new ArrayList<>();
            for (RoomDao roomDao : roomDaos) {
                rooms.add(RoomTransformer.transform(roomDao));
            }
            return new ResponseEntity(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity createRoom(Room room) {
        try {
            String hotelId = room.getId().getHotelId();
            if (room == null) {
                return new ResponseEntity("Must provide a valid room object", HttpStatus.BAD_REQUEST);
            }
            if (room.getId() == null) {
                return new ResponseEntity("Room must be associated with a hotel", HttpStatus.BAD_REQUEST);
            }
            if(!hotelService.hotelExists(hotelId)){
                return new ResponseEntity(hotelId + " is not a valid hotel ID", HttpStatus.BAD_REQUEST);
            }
            RoomDao roomDao = RoomTransformer.transform(room);
            if (roomRepository.exists(roomDao.getId())) {
                return new ResponseEntity("Room already exists", HttpStatus.BAD_REQUEST);
            }
            roomRepository.insert(roomDao);
            return new ResponseEntity("New room successfully added. Id: " + roomDao.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getRoom(String hotelId, int roomId) {
        try{
            if(hotelId == null){
                return new ResponseEntity("Must supply valid room id", HttpStatus.BAD_REQUEST);
            }
            CompositeKey compositeKey = new CompositeKey(hotelId, roomId);
            RoomDao roomDao = roomRepository.findOne(compositeKey);
            if(roomDao == null) {
                return new ResponseEntity("Could not find room with specified ID: " + compositeKey.toString(), HttpStatus.BAD_REQUEST);
            }
            Room room = RoomTransformer.transform(roomDao);
            return new ResponseEntity(room, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean roomExists(CompositeKey roomId) {
        return roomRepository.exists(roomId);
    }

    public String getRoomName(CompositeKey roomId) {
        RoomDao roomDao = roomRepository.getRoomNameById(roomId);
        if(roomDao.getName() != null) return roomDao.getName();
        else throw new NotFoundException();
    }
}
