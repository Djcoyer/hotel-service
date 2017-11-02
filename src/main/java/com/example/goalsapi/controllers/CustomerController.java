package com.example.goalsapi.controllers;

import com.example.goalsapi.models.Customer;
import com.example.goalsapi.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @GetMapping("/{customerId}")
    public ResponseEntity getCustomer(@PathVariable String customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping()
    public ResponseEntity createCustomer(@RequestBody Customer customer)  {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/{customerId}/bookings")
    public ResponseEntity getCustomerBookings(@PathVariable String customerId) {
        return customerService.getCustomerBookings(customerId);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity patchCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerService.patchCustomer(customerId, customer);
    }
}
