package com.leo.web;

import com.leo.dao.CustomerRepository;
import com.leo.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WebController {
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/create")
    public void createCustomer(@RequestBody Customer customer) {
        customerRepository.save(customer);
    }

    @GetMapping("/query")
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @PostMapping("/msg")
    public void create(@RequestParam String msg) {
        jmsTemplate.convertAndSend("customer:msg:new", msg);
    }

    @GetMapping("/msg")
    public String getMsg() {
        jmsTemplate.setReceiveTimeout(3000);
        Object reply = jmsTemplate.receiveAndConvert("customer:msg:new");
        return String.valueOf(reply);
    }
}
