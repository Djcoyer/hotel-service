package com.example.goalsapi;

import com.example.goalsapi.models.dao.HotelDao;
import com.example.goalsapi.services.HotelService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HotelServiceTest {

    @InjectMocks
    HotelService hotelService;

    @Autowired
    WebApplicationContext wac;

    private HotelDao hotelDao;
    private String guid = "900c61e4-bfd3-11e7-abc4-cec278b6b50a";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

}
