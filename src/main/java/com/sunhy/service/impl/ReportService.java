package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.Report;
import com.sunhy.mapper.ReportMapper;
import com.sunhy.service.IReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportService extends ServiceImpl<ReportMapper, Report> implements IReportService {

}
