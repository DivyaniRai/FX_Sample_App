package com.trade.service.trade_service.service;

import com.trade.service.trade_service.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer c);
    Customer get(String id);
    List<Customer> list();
    Customer update(String id, Customer c);
    void delete(String id);
}
