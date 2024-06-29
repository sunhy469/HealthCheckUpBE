package com.sunhy.dto;

import com.sunhy.entity.User;
import lombok.Data;


@Data
public class UserDto extends User {
    private String captcha;
    private String token;
}
