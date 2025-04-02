package com.bookstore.webbff.service;

import com.bookstore.webbff.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer-service", url = "${backend.base.url}")
public interface CustomerService {
    @GetMapping("/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") String id);

    @GetMapping("/customers")
    CustomerDTO getCustomerByUserId(@RequestParam("userId") String userId);
}