package com.sunhy.controller;

import com.sunhy.common.R;
import com.sunhy.entity.Orders;
import com.sunhy.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private IOrdersService orderService;

    public HomeController(IOrdersService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/apply")
    public R<String> apply(@RequestBody Orders order) {
        log.info("预约");
        orderService.save(order);
        return R.success("预约成功");
    }

}

