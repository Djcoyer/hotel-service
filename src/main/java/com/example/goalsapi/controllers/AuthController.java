package com.example.goalsapi.controllers;

import com.example.goalsapi.models.auth.AuthTokens;
import com.example.goalsapi.models.auth.LoginInfo;
import com.example.goalsapi.services.AuthService;
import com.example.goalsapi.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/auth")
public class AuthController {

    private AuthService authService;
    private CustomerService customerService;

    @Autowired
    public AuthController(AuthService authService, CustomerService customerService){
        this.authService = authService;
        this.customerService = customerService;
    }

    @PostMapping(path="/login", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public AuthTokens login (@RequestBody LoginInfo loginInfo) {
        return customerService.login(loginInfo);
    }

    @PostMapping(path="/logout", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader String authorization){
        customerService.logout(authorization);
    }

}
