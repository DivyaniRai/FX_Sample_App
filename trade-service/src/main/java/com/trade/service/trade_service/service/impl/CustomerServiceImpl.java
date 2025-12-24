package com.trade.service.trade_service.service.impl;

import com.trade.service.trade_service.model.Customer;
import com.trade.service.trade_service.repository.CustomerRepository;
import com.trade.service.trade_service.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    public CustomerServiceImpl(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public Customer create(Customer c) {
        return repo.save(c);
    }

    @Override
    public Customer get(String id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    @Override
    public List<Customer> list() {
        return repo.findAll();
    }

    @Override
    public Customer update(String id, Customer c) {
        Customer existing = get(id);
        existing.setName(c.getName());
        return repo.save(existing);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }
}
