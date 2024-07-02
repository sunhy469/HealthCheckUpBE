package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.Department;
import com.sunhy.mapper.DepartmentMapper;
import com.sunhy.service.IDepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
}
