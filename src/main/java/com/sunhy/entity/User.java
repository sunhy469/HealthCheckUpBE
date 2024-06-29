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

    @TableField(fill = FieldFill.INSERT)//插入时自动填充
    private LocalDateTime createTime;

}
