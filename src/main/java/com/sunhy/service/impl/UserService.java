package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.User;
import com.sunhy.mapper.UserMapper;
import com.sunhy.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
}
