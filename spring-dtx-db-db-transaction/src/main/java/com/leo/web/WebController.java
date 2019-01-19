package com.leo.web;

import com.leo.domain.Order;
import com.leo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WebController {
    @Autowired
    @Qualifier("customerService")
    private CustomerService customerService;

    @GetMapping(value = "/test")
    @ResponseBody
    public String createOrder(Integer order){
        return "测试成功";
    }

    @PostMapping("/customer/order")
    public void createOrder(@RequestBody Order order){
        customerService.createOrder(order);
    }
}
