package com.sunhy.controller;

import com.sunhy.service.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private IReportService reportService;

    public HomeController(IReportService reportService) {
        this.reportService = reportService;
    }


}

