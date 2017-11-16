package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.Exceptions.NotFoundException;
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
import static org.junit.Assert.*;
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
        try {
            when(hotelRepository.findOne(guid)).thenReturn(hotelDao);
            Hotel hotel = hotelService.getHotel(guid);
            assertNotNull("A valid ID should return a hotel object.", hotel);

            if (hotel != null) {
                assertEquals(guid, hotel.getId());
                assertEquals(12345, hotel.getZipCode());
                assertEquals(4, hotel.getRating(), .0001);
            }
        } catch (Exception e) {
            System.exit(2);
        }
    }

    @Test(expected = NotFoundException.class)
    public void getHotel_throwsNotFound_InvalidId() {
        //arrange
        when(hotelRepository.findOne(badGuid)).thenReturn(null);

        //act
        Hotel hotel = hotelService.getHotel(badGuid);

    }

    @Test(expected = InvalidInputException.class)
    public void getHotel_throwsInvalidInput_NullId() {
        hotelService.getHotel(null);
    }

    //region CREATE HOTEL

    @Test
    public void addHotel_returnsHotel_ValidObject() {

        //arrange
        when(hotelRepository.insert(hotelDao)).thenReturn(null);

        //act
        Hotel hotel = createHotel();
        Hotel _hotel = hotelService.addHotel(hotel);

        //assert
        assertNotNull(_hotel);
        assertEquals(hotel.getId(), _hotel.getId());
    }

    @Test(expected = InvalidInputException.class)
    public void addHotel_throwsInvalidInput_NullHotel(){
        hotelService.addHotel(new Hotel());
    }

    //endregion

    @Test
    public void getAllHotels_ReturnsHotelList() {

        //arrange
        List<HotelDao> hotelDaos = getHotelList();
        when(hotelRepository.findAll()).thenReturn(hotelDaos);

        //act
        List<Hotel> hotels = hotelService.getAllHotels();

        //assert
        assertNotNull(hotels);
        assertEquals(2, hotels.size());

    }

    @Test
    public void hotelExists_ReturnsTrue_ValidId(){

        //arrange
        when(hotelRepository.exists(guid)).thenReturn(true);

        //act
        boolean exists = hotelService.hotelExists(guid);

        //assert
        assertTrue(exists);
    }

    @Test
    public void hotelExists_ReturnsFalse_InvalidId(){

        //arrange
        when(hotelService.hotelExists(badGuid)).thenReturn(false);

        //act
        boolean exists = hotelService.hotelExists(badGuid);

        //assert
        assertFalse(exists);
    }

    //region HELPERS

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

    private List<HotelDao> getHotelList() {
        List<HotelDao> hotelDaos = new ArrayList<>();
        hotelDaos.add(hotelDao);
        hotelDaos.add(new HotelDao("1234567890", "206 S Houston School Rd", "Coyer Hotel",
                "Lancaster", "Texas", "Coyer hotel for anyone", 75146, 4));
        return hotelDaos;
    }

    //endregion

}
