package com.sunhy.controller;

import com.sunhy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private IOrderService orderService;

    public HomeController(IOrderService orderService) {
        this.orderService = orderService;
    }



}

