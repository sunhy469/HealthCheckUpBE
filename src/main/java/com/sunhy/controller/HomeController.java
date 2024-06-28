package com.sunhy.controller;

import com.sunhy.entity.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @PostMapping()
    public String home(@RequestBody Test test){
        log.info("id是"+test.getId());
        return "载入成功";
    }
}
