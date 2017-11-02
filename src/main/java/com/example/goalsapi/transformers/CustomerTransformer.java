package com.example.goalsapi.transformers;

import com.example.goalsapi.models.Customer;
import com.example.goalsapi.models.dao.CustomerDao;

public class CustomerTransformer {

    public static Customer transform(CustomerDao customerDao) {
        try{
            Customer customer = new Customer();
            customer.setCustomerId(customerDao.getCustomerId());
            customer.setBirthday(customerDao.getBirthday());
            customer.setEmailAddress(customerDao.getEmailAddress());
            customer.setFirstName(customerDao.getFirstName());
            customer.setLastName(customerDao.getLastName());
            return customer;
        }catch(Exception e){
            throw e;
        }
    }

    public static CustomerDao transform(Customer customer) {
        try{
            CustomerDao customerDao = new CustomerDao();
            customerDao.setBirthday(customer.getBirthday());
            customerDao.setCustomerId(customer.getCustomerId());
            customerDao.setEmailAddress(customer.getEmailAddress());
            customerDao.setFirstName(customer.getFirstName());
            customerDao.setLastName(customer.getLastName());
            customerDao.setCustomerId(customer.getCustomerId());
            return customerDao;
        }catch(Exception e) {
            throw e;
        }
    }
}
