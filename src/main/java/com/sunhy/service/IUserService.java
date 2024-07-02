package com.sunhy.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunhy.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface IUserService extends IService<User> {
    List<User> getPatient(LambdaQueryWrapper<User> wrapper);
}
