package com.sunhy.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String userid;

    private String nickname;

    private String avatar;

    private String sign;

    private String birthday;

    private String email;

    private String wechat;

    private LocalDateTime updateTime;
}
