package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Customer;
import com.example.goalsapi.services.AuthService;
import com.example.goalsapi.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomer(@RequestHeader String authorization) {
        return customerService.getCustomerFromHeader(authorization);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Customer createCustomer(@RequestBody Customer customer)  {
        return customerService.createCustomer(customer);
    }

//    @GetMapping("/{customerId}/bookings")
//    public ResponseEntity getCustomerBookings(@PathVariable String customerId) {
//        return customerService.getCustomerBookings(customerId);
//    }

    @PatchMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer patchCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerService.patchCustomer(customerId, customer);
    }
}
