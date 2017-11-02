package com.example.goalsapi.services;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import com.auth0.net.SignUpRequest;
import com.example.goalsapi.models.auth.AuthTokens;
import com.example.goalsapi.models.auth.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    CustomerService customerService;

    private AuthAPI authApi;
    private String domain = "dcoyer-goals.auth0.com";
    private String clientId = "PPk4hLnit5ynshfbSsrsy8wgbLzlJqpc";
    private String clientSecret = "UkunYpteLJHYUloAKNPalb0XYeJ06j7xUM8HHYX3kiLM8BIlVN_GgYD13AOVyxUa";


    public ResponseEntity login(LoginInfo loginInfo) {
        if(loginInfo.getUsername() == null || loginInfo.getPassword() == null) {
            return new ResponseEntity("Must provide valid credentials", HttpStatus.BAD_REQUEST);
        }

        authApi = new AuthAPI(domain, clientId, clientSecret);
        AuthRequest request = authApi.login(loginInfo.getUsername(), loginInfo.getPassword(), "Username-Password-Authentication");

        try{
            TokenHolder holder = request.execute();
            AuthTokens tokens = getTokens(holder);
            return new ResponseEntity(tokens, HttpStatus.OK);
        } catch (Auth0Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private AuthTokens getTokens(TokenHolder holder){
        AuthTokens tokens = new AuthTokens();
        tokens.setAccess_token(holder.getAccessToken());
        tokens.setRefresh_token(holder.getRefreshToken());
        tokens.setExpires_in(holder.getExpiresIn());
        tokens.setId_token(holder.getIdToken());
        return tokens;
    }
}
