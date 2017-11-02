package com.example.goalsapi.services;

import com.example.goalsapi.models.Booking;
import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.dao.CustomerDao;
import com.example.goalsapi.repositories.CustomerRepository;
import com.example.goalsapi.transformers.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingService bookingService;

    public CustomerService(CustomerRepository customerRepository, BookingService bookingService) {
        this.customerRepository = customerRepository;
        this.bookingService = bookingService;
    }

    public ResponseEntity getCustomer(String customerId) {
        try{
            CustomerDao customerDao = customerRepository.findOne(customerId);
            if(customerDao != null && customerDao.getCustomerId() != null) {
                Customer customer = CustomerTransformer.transform(customerDao);
                return new ResponseEntity(customer, HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Must supply valid customerId", HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e) {
            throw e;
        }

    }

    public ResponseEntity createCustomer(Customer customer) {
        try{
            if(customer != null) {
                customer.setCustomerId(UUID.randomUUID().toString());
                CustomerDao customerDao = CustomerTransformer.transform(customer);
                customerRepository.insert(customerDao);
                return new ResponseEntity(customerDao.getCustomerId(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Must supply a customer object", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            throw e;
        }
    }

    public ResponseEntity getCustomerBookings(String customerId) {
        if(getCustomer(customerId) == null) {
            return new ResponseEntity("Must provide valid Customer ID", HttpStatus.BAD_REQUEST);
        }
        List<Booking> customerBookings = bookingService.getBookingsByCustomerId(customerId);
        if(customerBookings == null || customerBookings.size() == 0) {
            return new ResponseEntity("No bookings found for this customer", HttpStatus.OK);
        }
        return new ResponseEntity(customerBookings, HttpStatus.OK);
    }

    public ResponseEntity patchCustomer(String customerId, Customer customer) {
        if(customerId == null || customer == null) {
            return new ResponseEntity("Must provide valid input for customer and customer ID", HttpStatus.BAD_REQUEST);
        }
        if(!customerRepository.exists(customerId)){
            return new ResponseEntity("Invalid Customer ID", HttpStatus.BAD_REQUEST);
        }
        customer.setCustomerId(customerId);
        CustomerDao _customerDao = CustomerTransformer.transform(customer);
        customerRepository.save(_customerDao);
        return new ResponseEntity(_customerDao, HttpStatus.OK);
    }
}
