package com.example.goalsapi.controllers;

import com.example.goalsapi.models.auth.LoginInfo;
import com.example.goalsapi.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(path="/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity login (@RequestBody LoginInfo loginInfo) {
        return authService.login(loginInfo);
    }

}
