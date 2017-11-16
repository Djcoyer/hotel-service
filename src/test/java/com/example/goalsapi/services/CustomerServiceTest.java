package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.ForbiddenException;
import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.Exceptions.NotFoundException;
import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.auth.AuthTokens;
import com.example.goalsapi.models.auth.LoginInfo;
import com.example.goalsapi.models.dao.CustomerDao;
import com.example.goalsapi.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;


    @Autowired
    WebApplicationContext wac;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuthService authService;

    private CustomerDao customerDao;

    private String goodGuid = "42ea41f6-bfec-11e7-abc4-cec278b6b50a";
    private String badGuid = "12345";
    private LoginInfo goodLoginInfo = new LoginInfo("goodusername","realpassword");
    private LoginInfo badLoginInfo = new LoginInfo("badusername","fakepassword");
    private String goodAuthHeader = "Bearer someweirdidtokenthatistrue";
    private String badAuthHeader = "Bearer someweirdtokenthatisfalse";

    @Before
    public void init(){
        customerService = new CustomerService(customerRepository, authService);
        customerDao = new CustomerDao();
        customerDao.setFirstName("Devyn");
        customerDao.setLastName("Coyer");
        customerDao.setEmailAddress("dcoyer@lwolf.com");
        customerDao.setBirthday(new Date(1997,8,8));
        customerDao.setCustomerId(goodGuid);
        when(customerRepository.findOne(goodGuid)).thenReturn(customerDao);
    }

    //region GET CUSTOMER
    @Test
    public void getCustomer_ReturnsCustomer_ValidId(){
        when(customerRepository.exists(goodGuid)).thenReturn(true);
        when(customerRepository.findOne(goodGuid)).thenReturn(customerDao);
        Customer customer = customerService.getCustomerById(goodGuid);
        assertNotNull("Returned customer should not be null",customer);
        assertEquals(customer.getCustomerId(), goodGuid);
        assertEquals(customer.getBirthday(), customerDao.getBirthday());
    }

    @Test(expected = NotFoundException.class)
    public void getCustomerById_ThrowsNotFoundException_InvalidId(){
        when(customerRepository.findOne(badGuid)).thenReturn(null);
        Customer customer  = customerService.getCustomerById(badGuid);
    }

    @Test
    public void getCustomerFromHeader_ReturnsCustomer_ValidHeader(){

        //arrange
        when(authService.getCustomerIdFromAuthorizationHeader(goodAuthHeader)).thenReturn(goodGuid);
        when(customerRepository.findOne(goodGuid)).thenReturn(customerDao);

        //act
        Customer customer = customerService.getCustomerFromHeader(goodAuthHeader);

        //assert
        assertNotNull(customer);
        assertEquals(goodGuid, customer.getCustomerId());
    }

    @Test(expected = NotFoundException.class)
    public void getCustomerFromHeader_ThrowsNotFound_InvalidHeaderId(){

        //arrange
        when(authService.getCustomerIdFromAuthorizationHeader(goodAuthHeader)).thenReturn(badGuid);
        when(customerRepository.findOne(goodGuid)).thenReturn(null);

        //act
        Customer customer = customerService.getCustomerFromHeader(badAuthHeader);
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
        when(customerRepository.exists(goodGuid)).thenReturn(true);
        when(customerRepository.save(customerDao)).thenReturn(customerDao);
        Customer customer = createCustomerObject();
        Customer _customer = customerService.patchCustomer(goodGuid, customer);
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

    //region AUTH

    //region LOGIN

    @Test
    public void login_returnsAuthTokens_ValidRequest(){

        //arrange
        AuthTokens authTokens = new AuthTokens();
        authTokens.setId_token("12345");
        authTokens.setRefresh_token("refreshingthis");
        authTokens.setAccess_token("accesstoken");
        when(authService.login(goodLoginInfo)).thenReturn(authTokens);
        when(authService.getCustomerIdFromJwt("12345")).thenReturn(goodGuid);
        when(customerRepository.findOne(goodGuid)).thenReturn(customerDao);
        when(customerRepository.save(customerDao)).thenReturn(null);

        //act
        AuthTokens _authTokens = customerService.login(goodLoginInfo);

        //assert
        assertEquals("idToken should match",authTokens.getId_token(), _authTokens.getId_token());
        assertEquals(authTokens.getRefresh_token(), _authTokens.getRefresh_token());
    }

    @Test(expected = InvalidInputException.class)
    public void login_throwsInvalidInput_BadLoginInfo(){

        //arrange
        doThrow(new InvalidInputException()).when(authService).login(badLoginInfo);

        //act
        customerService.login(badLoginInfo);
    }

    @Test(expected = ForbiddenException.class)
    public void login_throwsForbidden_AuthTokensNull(){

        //arrange
        AuthTokens authTokens = new AuthTokens();
        authTokens.setId_token(null);
        authTokens.setRefresh_token(null);
        authTokens.setAccess_token(null);
        when(authService.login(badLoginInfo)).thenReturn(authTokens);

        //act
        customerService.login(badLoginInfo);

    }

    //endregion

    //region LOGOUT

    @Test
    public void logout_revokesRefreshToken_validHeader(){

        //arrange
        customerDao.setRefreshToken("refreshtoken");
        when(authService.getCustomerIdFromAuthorizationHeader(goodAuthHeader)).thenReturn(goodGuid);
        when(customerRepository.getRefreshToken(goodGuid)).thenReturn(customerDao);
        when(customerRepository.save(customerDao)).thenReturn(null);

        //act
        customerService.logout(goodAuthHeader);
    }

    //endregion

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
        customer.setCustomerId(goodGuid);
        customer.setFirstName(customerDao.getFirstName());
        customer.setLastName(customerDao.getLastName());
        customer.setEmailAddress(customerDao.getEmailAddress());
        customer.setBirthday(customerDao.getBirthday());
        return customer;
    }

    public List<Booking> getBookingObjects() {
        List<Booking> bookings = new ArrayList<Booking>();
        bookings.add(new Booking(goodGuid, new Date(), new Date(), badGuid, 1));
        bookings.add(new Booking(goodGuid, new Date(), new Date(), badGuid, 2));
        return bookings;
    }

    //endregion
}
