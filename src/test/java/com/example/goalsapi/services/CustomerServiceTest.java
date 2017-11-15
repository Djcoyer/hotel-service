package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.Exceptions.NotFoundException;
import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.dao.CustomerDao;
import com.example.goalsapi.repositories.CustomerRepository;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private BookingService bookingService;

    @Autowired
    WebApplicationContext wac;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuthService authService;

    private CustomerDao customerDao;

    private String guid = "42ea41f6-bfec-11e7-abc4-cec278b6b50a";
    private String badGuid = "12345";

    @Before
    public void init(){
        customerService = new CustomerService(customerRepository, authService);
        customerDao = new CustomerDao();
        customerDao.setFirstName("Devyn");
        customerDao.setLastName("Coyer");
        customerDao.setEmailAddress("dcoyer@lwolf.com");
        customerDao.setBirthday(new Date(1997,8,8));
        customerDao.setCustomerId(guid);
        when(customerRepository.findOne(guid)).thenReturn(customerDao);
    }


    //region GET CUSTOMER
    @Test
    public void getCustomer_ReturnsCustomer_ValidId(){
        when(customerRepository.exists(guid)).thenReturn(true);
        when(customerRepository.findOne(guid)).thenReturn(customerDao);
        Customer customer = customerService.getCustomerById(guid);
        assertNotNull("Returned customer should not be null",customer);
        assertEquals(customer.getCustomerId(), guid);
        assertEquals(customer.getBirthday(), customerDao.getBirthday());
    }

    @Test(expected = NotFoundException.class)
    public void getCustomerById_ThrowsNotFoundException_InvalidId(){
        when(customerRepository.findOne(badGuid)).thenReturn(null);
        Customer customer  = customerService.getCustomerById(badGuid);
    }

    //endregion

    //region CREATE CUSTOMER

    @Test
    public void createCustomer_ReturnsSameCustomer() {
        when(customerRepository.insert(customerDao)).thenReturn(null);
        Customer customer = createCustomerObject();
        customer.setCustomerId(null);
        Customer createdCustomer = customerService.createCustomer(customer);
        assertEquals("Created customer should have same email as passed in",
                customer.getEmailAddress(), createdCustomer.getEmailAddress());
        assertEquals(customer.getFirstName(), createdCustomer.getFirstName());
    }

    @Test(expected = InvalidInputException.class)
    public void createCustomer_ThrowsInvalidInput_NullCustomer() {
        Customer customer = customerService.createCustomer(null);
    }

    //endregion

    //region UPDATE CUSTOMER
    @Test
    public void patchCustomer_ReturnsUpdatedCustomer_ValidRequest() {
        when(customerRepository.exists(guid)).thenReturn(true);
        when(customerRepository.save(customerDao)).thenReturn(customerDao);
        Customer customer = createCustomerObject();
        Customer _customer = customerService.patchCustomer(guid, customer);
        assertNotNull(_customer);
    }

    @Test(expected = InvalidInputException.class)
    public void patchCustomer_ThrowsInvalidInput_NullId() {
        Customer customer = createCustomerObject();
        Customer _customer = customerService.patchCustomer(null, customer);
    }

    @Test(expected = NotFoundException.class)
    public void patchCustomer_ThrowsNotFound_InvalidId() {
        Customer customer = createCustomerObject();
        Customer _customer = customerService.patchCustomer(badGuid, customer);
    }

    //endregion

    @Test
    public void getUUID_returnsValidUUID() {
        String uuid = customerService.generateCustomerId();
        try{
            UUID _uuid = UUID.fromString(uuid);
            assertNotNull(_uuid);
        }catch(Exception e){
            System.exit(2);
        }
    }


    //region HELPERS
    private Customer createCustomerObject() {
        Customer customer = new Customer();
        customer.setCustomerId(guid);
        customer.setFirstName(customerDao.getFirstName());
        customer.setLastName(customerDao.getLastName());
        customer.setEmailAddress(customerDao.getEmailAddress());
        customer.setBirthday(customerDao.getBirthday());
        return customer;
    }

    public List<Booking> getBookingObjects() {
        List<Booking> bookings = new ArrayList<Booking>();
        bookings.add(new Booking(guid, new Date(), new Date(), badGuid, 1));
        bookings.add(new Booking(guid, new Date(), new Date(), badGuid, 2));
        return bookings;
    }

    //endregion
}
