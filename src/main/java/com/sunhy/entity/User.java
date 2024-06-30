package com.sunhy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String userid;

    private String username;

    private String password;

    private String mobile;

    private String sex;

    // 权限id
    // 0--- 普通用户
    // 1---  医生
    // 2---  管理员
    private Long roleId;

    @TableField(fill = FieldFill.INSERT)//插入时自动填充
    private LocalDateTime createTime;

}
