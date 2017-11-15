package com.example.goalsapi.services;

import com.example.goalsapi.models.Hotel;
import com.example.goalsapi.models.dao.HotelDao;
import com.example.goalsapi.repositories.HotelRepository;
import com.example.goalsapi.services.HotelService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HotelServiceTest {

    @InjectMocks
    HotelService hotelService;

    @Autowired
    WebApplicationContext wac;

    @Mock
    private HotelRepository hotelRepository;

    private HotelDao hotelDao;
    private String guid = "900c61e4-bfd3-11e7-abc4-cec278b6b50a";
    private String badGuid = "123456";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        hotelService = new HotelService(hotelRepository);

        hotelDao = new HotelDao();
        hotelDao.setId(guid);
        hotelDao.setDescription("Test hotel for testing purposes");
        hotelDao.setAddress("123 Nowhere Street");
        hotelDao.setCity("Sometown");
        hotelDao.setState("SomeState");
        hotelDao.setName("My Hotel");
        hotelDao.setZipCode(12345);
        hotelDao.setRating(4);
    }

    @Test
    public void getHotel_ReturnsHotel_ValidId() {
        try{
            when(hotelRepository.findOne(guid)).thenReturn(hotelDao);
            ResponseEntity response = hotelService.getHotel(guid);
            assertEquals(200, response.getStatusCodeValue());
            Hotel hotel = (Hotel)response.getBody();
            assertNotNull("A valid ID should return a hotel object.", hotel);

            if(hotel != null) {
                assertEquals(guid, hotel.getId());
                assertEquals(12345, hotel.getZipCode());
                assertEquals(4, hotel.getRating(), .0001);
            }
        }catch(Exception e) {
            System.exit(2);
        }
    }

    @Test
    public void getHotel_Returns400_InvalidId() {
        try{
            when(hotelRepository.findOne(badGuid)).thenReturn(null);
            ResponseEntity response = hotelService.getHotel(badGuid);
            assertEquals(400, response.getStatusCodeValue());
        }catch(Exception e) {
            System.exit(2);
        }
    }

    @Test
    public void addHotel_ReturnsId_ValidHotel() {
        try {
            when(hotelRepository.insert(hotelDao)).thenReturn(null);
            Hotel hotel = createHotel();
            ResponseEntity response = hotelService.addHotel(hotel);
            HotelDao hotelDao = (HotelDao) response.getBody();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(hotelDao.getAddress(), hotel.getAddress());
        } catch (Exception e) {
            System.exit(2);
        }
    }

    @Test
    public void getAllHotels_ReturnsHotelList() {
        try{
            List<HotelDao> hotelDaos = getHotelList();
            when(hotelRepository.findAll()).thenReturn(hotelDaos);
            ResponseEntity response = hotelService.getAllHotels();
            assertEquals(200, response.getStatusCodeValue());
            List<Hotel> hotels = (List<Hotel>)response.getBody();
            assertNotNull(hotels);
            assertEquals(2, hotels.size());

        }catch(Exception e){
            System.exit(2);
        }
    }

    @Test
    public void getUUID_returnsValidUUID() {
        String uuid = hotelService.getUUID();
        try{
            UUID _uuid = UUID.fromString(uuid);
            assertNotNull(_uuid);
        }catch(Exception e){
            System.exit(2);
        }
    }

    private Hotel createHotel() {
            Hotel hotel = new Hotel();
            hotel.setZipCode(123455);
            hotel.setAddress("123 Nowhere Street");
            hotel.setCity("Sometown");
            hotel.setState("SomeState");
            hotel.setDescription("Test hotel for testing purposes");
            hotel.setRating(4);
            hotel.setId(guid);
            return hotel;
    }

    private List<HotelDao> getHotelList(){
        List<HotelDao> hotelDaos = new ArrayList<>();
        hotelDaos.add(hotelDao);
        hotelDaos.add(new HotelDao("1234567890","206 S Houston School Rd", "Coyer Hotel",
                "Lancaster", "Texas", "Coyer hotel for anyone", 75146, 4));
        return hotelDaos;
    }



}
