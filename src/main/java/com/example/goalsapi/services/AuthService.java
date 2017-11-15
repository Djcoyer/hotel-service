package com.example.goalsapi.services;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import com.auth0.net.SignUpRequest;
import com.example.goalsapi.Exceptions.ForbiddenException;
import com.example.goalsapi.Exceptions.InternalServerException;
import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.models.CustomInfo;
import com.example.goalsapi.models.auth.AuthTokens;
import com.example.goalsapi.models.auth.LoginInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AuthService {

    private AuthAPI authApi;
    private String domain = "dcoyer-goals.auth0.com";
    private String clientId = "PPk4hLnit5ynshfbSsrsy8wgbLzlJqpc";
    private String clientSecret = "UkunYpteLJHYUloAKNPalb0XYeJ06j7xUM8HHYX3kiLM8BIlVN_GgYD13AOVyxUa";

    @Autowired
    public AuthService(){
    }

    public AuthTokens login(LoginInfo loginInfo) {
        if(loginInfo.getUsername() == null || loginInfo.getUsername() == "" ||
                loginInfo.getPassword() == null || loginInfo.getPassword() == "") {
            throw new InvalidInputException();
        }

        authApi = new AuthAPI(domain, clientId, clientSecret);
        AuthRequest request = authApi.login(loginInfo.getUsername(), loginInfo.getPassword(),
                "Username-Password-Authentication").setScope("openid profile offline_access");

        try{
            TokenHolder holder = request.execute();
            AuthTokens tokens = getTokens(holder);
            return tokens;
        } catch (Auth0Exception e) {
            throw new ForbiddenException();
        }
    }

    public String getCustomerIdFromAuthorizationHeader(String authHeader){
        if(! authHeader.contains("Bearer"))
            throw new ForbiddenException();
        String[] authSections = authHeader.split(" ");
        String token = authSections[1];
        String customerId = getCustomerIdFromJwt(token);
        return customerId;
    }

    public String getCustomerIdFromJwt(String id_token){
        DecodedJWT jwt = JWT.decode(id_token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("http://customerInfo");
        if(claim == null) throw new ForbiddenException();
        CustomInfo info = claim.as(CustomInfo.class);
        String customerId = info.getUid();
        if(customerId != null) return customerId;
        else throw new ForbiddenException();
    }

    private AuthTokens getTokens(TokenHolder holder){
        AuthTokens tokens = new AuthTokens();
        tokens.setAccess_token(holder.getAccessToken());
        tokens.setRefresh_token(holder.getRefreshToken());
        tokens.setExpires_in(holder.getExpiresIn());
        tokens.setId_token(holder.getIdToken());
        return tokens;
    }

    public void revokeAuthRefreshToken(String refreshToken){
        try{
            authApi = new AuthAPI(domain, clientId, clientSecret);
            Request<Void> request = authApi.revokeToken(refreshToken);
            request.execute();
        }catch(Auth0Exception e){
            throw new InternalServerException();
        }

    }
}
