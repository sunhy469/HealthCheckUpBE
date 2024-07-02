package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.Doctor;
import com.sunhy.mapper.DoctorMapper;
import com.sunhy.service.IDoctorService;
import org.springframework.stereotype.Service;


@Service
public class DoctorService extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {
}
