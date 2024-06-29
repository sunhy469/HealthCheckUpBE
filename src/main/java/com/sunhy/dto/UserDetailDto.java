package com.sunhy.dto;


import com.sunhy.entity.UserDetail;
import lombok.Data;

@Data
public class UserDetailDto extends UserDetail {

    private String username;

    private String sex;
}
