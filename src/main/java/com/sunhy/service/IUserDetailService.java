package com.sunhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunhy.entity.UserDetail;

public interface IUserDetailService extends IService<UserDetail> {

    UserDetail getUserDetail(Long id);
}
