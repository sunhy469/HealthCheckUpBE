package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sunhy.common.BaseContext;
import com.sunhy.common.R;
import com.sunhy.dto.UserDto;
import com.sunhy.entity.Test;
import com.sunhy.entity.User;
import com.sunhy.service.IUserService;
import com.sunhy.utils.JavaWebToken;
import com.sunhy.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private final IUserService userService;


    private final RedisTemplate<String, String> redisTemplate;
    public HomeController(IUserService userService, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping()
    public String home(@RequestBody Test test){

        log.info("id是"+test.getId());
        return "载入成功";
    }


}

