package com.example.demo.rest;



import com.example.demo.domain.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class CommandeResource {

    @Autowired
    OrderService orderService;

    @PostMapping("/") @ResponseBody
    public Order save(@RequestBody @Valid Order order) {
        return orderService.save(order);
    }

    @GetMapping(path = "/Order")
    @PostAuthorize("hasAnyAuthority('User')")
    public List<Order> findAll(){
        return orderService.findAll();
    }
}
