package com.example.goalsapi.services;

import com.example.goalsapi.Exceptions.InternalServerException;
import com.example.goalsapi.Exceptions.InvalidInputException;
import com.example.goalsapi.Exceptions.NotFoundException;
import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.auth.AuthTokens;
import com.example.goalsapi.models.auth.LoginInfo;
import com.example.goalsapi.models.dao.CustomerDao;
import com.example.goalsapi.repositories.CustomerRepository;
import com.example.goalsapi.transformers.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private AuthService authService;
    @Autowired
    public CustomerService(CustomerRepository customerRepository, AuthService authService) {
        this.customerRepository = customerRepository;
        this.authService = authService;
    }

    public String generateCustomerId() {
        return UUID.randomUUID().toString();
    }

    public Customer getCustomerFromHeader(String authorization) {
        try{
            String customerId = authService.getCustomerIdFromAuthorizationHeader(authorization);
            CustomerDao customerDao = customerRepository.findOne(customerId);
            if(customerDao != null && customerDao.getCustomerId() != null) {
                Customer customer = CustomerTransformer.transform(customerDao);
                return customer;
            }
            else{
                throw new NotFoundException();
            }
        }catch(Exception e) {
            throw e;
        }

    }


    //region AUTH
    public AuthTokens login(LoginInfo loginInfo){
        AuthTokens authTokens = authService.login(loginInfo);
        assignRefreshTokenToUser(authTokens.getId_token(), authTokens.getRefresh_token());
        return authTokens;
    }

    private void assignRefreshTokenToUser(String id_token, String refresh_token) {
        if(id_token == null || refresh_token == null) return;
        String customerId = authService.getCustomerIdFromJwt(id_token);
        setRefreshToken(customerId, refresh_token);
    }

    public void logout(String authorization){
        String customerId = authService.getCustomerIdFromAuthorizationHeader(authorization);
        String refreshToken = getRefreshToken(customerId);
        authService.revokeAuthRefreshToken(refreshToken);
        revokeRefreshToken(customerId);
    }


    //endregion

    public Customer getCustomerById(String customerId){
        if(!customerRepository.exists(customerId)) throw new NotFoundException();
        CustomerDao customerDao = customerRepository.findOne(customerId);
        Customer customer = CustomerTransformer.transform(customerDao);
        return customer;
    }

    public Customer createCustomer(Customer customer) {
            if(customer != null) {
                CustomerDao customerDao = CustomerTransformer.transform(customer);
                customerRepository.insert(customerDao);
                return customer;
            }
            else{
                throw new InvalidInputException();
            }
        }

    public Customer patchCustomer(String customerId, Customer customer) {
        if(customerId == null || customer == null) {
            throw new InvalidInputException();
        }
        if(!customerRepository.exists(customerId)){
            throw new NotFoundException();
        }
        customer.setCustomerId(customerId);
        CustomerDao _customerDao = CustomerTransformer.transform(customer);
        customerRepository.save(_customerDao);
        return customer;
    }

    private CustomerDao getCustomerDaoById(String customerId){
        CustomerDao customerDao = customerRepository.findOne(customerId);
        if(customerDao.getEmailAddress() == null) throw new NotFoundException();
        return customerDao;
    }

    public void setRefreshToken(String customerId, String refresh_token) {
        CustomerDao customerDao = getCustomerDaoById(customerId);
        customerDao.setRefreshToken(refresh_token);
        customerRepository.save(customerDao);
    }

    public void revokeRefreshToken(String customerId){
        CustomerDao customerDao = getCustomerDaoById(customerId);
        customerDao.setRefreshToken(null);
        customerRepository.save(customerDao);
    }

    public String getRefreshToken(String customerId) {
        CustomerDao customerDao = customerRepository.getRefreshToken(customerId);
        String refreshToken = customerDao.getRefreshToken();
        return refreshToken;
    }
}
