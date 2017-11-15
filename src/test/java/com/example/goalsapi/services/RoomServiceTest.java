package com.example.goalsapi.services;

import com.example.goalsapi.models.CompositeKey;
import com.example.goalsapi.models.Room;
import com.example.goalsapi.models.dao.RoomDao;
import com.example.goalsapi.repositories.RoomRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelService hotelService;

    @Autowired
    private WebApplicationContext wac;

    private CompositeKey compositeKey = new CompositeKey("01825bc0-bff3-11e7-abc4-cec278b6b50a", 1);
    private String hotelId = "01825bc0-bff3-11e7-abc4-cec278b6b50a";
    private int roomNumber = 1;
    private String badHotelId = "123456";
    private CompositeKey badCompositeKey = new CompositeKey(badHotelId, roomNumber);

    private RoomDao roomDao;

    @Before
    public void init() {
        roomService = new RoomService(roomRepository, hotelService);
        roomDao = new RoomDao();
        roomDao.setName("Test Room");
        roomDao.setId(compositeKey);
        roomDao.setPricePerNight(40);
    }

    @Test
    public void getAllRooms_ReturnsRooms(){
        List<RoomDao> roomDaos = getRoomDaoList();
        when(roomRepository.findAllByHotelId(hotelId)).thenReturn(roomDaos);
        ResponseEntity response = roomService.getAllRooms(hotelId);
        assertEquals("200 Status Code should be returned on valid request",200,
                response.getStatusCodeValue());
        List<RoomDao> _roomDaos = (List<RoomDao>)response.getBody();
        assertEquals(roomDaos.size(), _roomDaos.size());
    }

    @Test
    public void getAllRooms_Returns400Status_InvalidHotelId(){
        when(roomRepository.findAllByHotelId(badHotelId)).thenReturn(new ArrayList<RoomDao>());
        ResponseEntity response = roomService.getAllRooms(badHotelId);
        assertEquals("400 status code should be returned for invalid ID",400,
                response.getStatusCodeValue());
    }

    @Test
    public void createRoom_Returns200Status_ValidRoom(){
        Room room = createRoomObject();
        when(hotelService.hotelExists(hotelId)).thenReturn(true);
        when(roomRepository.insert(roomDao)).thenReturn(null);
        when(roomRepository.exists(roomDao.getId())).thenReturn(false);
        ResponseEntity response = roomService.createRoom(room);

        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody().toString(), containsString(room.getId().toString()));
    }

    @Test
    public void createRoom_Returns400Status_InvalidHotelId(){
        when(hotelService.hotelExists(badHotelId)).thenReturn(false);
        Room room = createBadRoomObject();
        ResponseEntity response = roomService.createRoom(room);

        assertEquals(400, response.getStatusCodeValue());
        assertThat(response.getBody().toString(), containsString(badHotelId));
    }

    @Test
    public void getRoom_ReturnsRoom_ValidId(){
        when(roomRepository.findOne(compositeKey)).thenReturn(roomDao);
        ResponseEntity response = roomService.getRoom(hotelId, roomNumber);
        Room room = (Room)response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roomDao.getName(), room.getName());
        assertEquals(roomDao.getId(), room.getId());
        assertEquals(roomDao.getPricePerNight(), room.getPricePerNight(), 0.1);
    }

    @Test
    public void getRoom_Returns400Status_InvalidId(){
        when(roomRepository.findOne(badCompositeKey)).thenReturn(null);
        ResponseEntity responseEntity = roomService.getRoom(badHotelId, roomNumber);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertThat(responseEntity.getBody().toString(), containsString(badCompositeKey.toString()));
    }


    private List<RoomDao> getRoomDaoList(){
        List<RoomDao> roomDaos = new ArrayList<>();
        roomDaos.add(new RoomDao(new CompositeKey("12345", 2), "Test Room 2",
                40, "Great thingy"));
        roomDaos.add(roomDao);
        return roomDaos;
    }

    private Room createRoomObject(){
        Room room = new Room();
        room.setName(roomDao.getName());
        room.setId(compositeKey);
        room.setPricePerNight(room.getPricePerNight());
        return room;
    }

    private Room createBadRoomObject(){
        Room room = new Room();
        room.setName(roomDao.getName());
        room.setId(badCompositeKey);
        room.setPricePerNight(room.getPricePerNight());
        return room;
    }

}
