package com.example.goalsapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Date birthday;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String auth0Id;

    public Customer(){}

    public Customer(String firstName, String lastName,  String emailAddress, Date birthday, String auth0Id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.birthday = birthday;
        this.auth0Id = auth0Id;
    }

    public Customer(String customerId, String firstName, String lastName,  String emailAddress, Date birthday, String auth0Id) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.birthday = birthday;
        this.auth0Id = auth0Id;
    }

}
