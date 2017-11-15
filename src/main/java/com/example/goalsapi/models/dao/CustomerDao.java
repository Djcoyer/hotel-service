package com.example.goalsapi.models.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@Document(collection="customers")
public class CustomerDao {
    @Id
    private String customerId;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String emailAddress;
    private String refreshToken;

    public CustomerDao(){}

    public CustomerDao(String firstName, String lastName, Date birthday, String emailAddress, String password) {
        this.customerId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.emailAddress = emailAddress;
    }

    public CustomerDao(String customerId, String firstName, String lastName, Date birthday, String emailAddress, String password) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.emailAddress = emailAddress;
    }
}
