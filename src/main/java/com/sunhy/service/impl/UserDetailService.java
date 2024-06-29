package com.sunhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.User;
import com.sunhy.entity.UserDetail;
import com.sunhy.mapper.UserDetailMapper;
import com.sunhy.service.IUserDetailService;
import com.sunhy.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService extends ServiceImpl<UserDetailMapper, UserDetail> implements IUserDetailService {

    private final IUserService userService;

    public UserDetailService(IUserService userService) {
        this.userService = userService;
    }

    // 根据 user表的id查询对应的userDetail对象
    @Override
    public UserDetail getUserDetail(Long id) {
        User user = userService.getById(id);
        String userid = user.getUserid();
        LambdaQueryWrapper<UserDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDetail::getUserid,userid);
        return this.getOne(wrapper);
    }
}
