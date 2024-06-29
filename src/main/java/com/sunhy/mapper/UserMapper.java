package com.sunhy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunhy.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
