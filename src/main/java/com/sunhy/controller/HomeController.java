package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sunhy.common.R;
import com.sunhy.dto.RecordDto;
import com.sunhy.dto.UserDto;
import com.sunhy.entity.Department;
import com.sunhy.entity.Doctor;
import com.sunhy.entity.Orders;
import com.sunhy.entity.User;
import com.sunhy.service.IDepartmentService;
import com.sunhy.service.IDoctorService;
import com.sunhy.service.IOrdersService;
import com.sunhy.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private final IOrdersService orderService;

    private final IDoctorService doctorService;

    private final IDepartmentService departmentService;


    private final IUserService userService;

    public HomeController(IOrdersService orderService, IDoctorService doctorService, IDepartmentService departmentService, IUserService userService) {
        this.orderService = orderService;
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @PostMapping("/finishrecord")
    public R<String> finishRecord(@RequestBody Orders order) {
        log.info("结束预约");
        Orders one = orderService.getById(order);
        if (one == null){
            return R.error("订单错误");
        }else {
            one.setIsFinished(one.getIsFinished()+1);
            orderService.updateById(one);
            return R.success("完成订单");
        }
    }

    @PostMapping("/apply")
    public R<String> apply(@RequestBody Orders order) {
        log.info("预约");
        String doctorName = order.getDoctorName();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName,doctorName);
        User user = userService.getOne(wrapper);
        order.setDoctorId(user.getId());
        order.setIsFinished(0);
        orderService.save(order);
        return R.success("预约成功");
    }

    @PostMapping("/recordall")
        public R<List<RecordDto>> recordAll(@RequestBody User user) {
        log.info("记录所有");
        Long roleId = user.getRoleId();
        Long id = user.getId(); // 医生或者管理员

        List<Orders> orders = null;
        if (roleId ==2 && id != null){
            // 管理员
            orders = orderService.list();
        }else if (roleId ==1 && id != null){
            // 医生
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getDoctorId,id);
            orders = orderService.list(wrapper);
        }else {
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getUserId,id);
            orders = orderService.list(wrapper);
        }

        ArrayList<RecordDto> records = new ArrayList<>();
        for (Orders order : orders) {
            RecordDto recordDto = new RecordDto();

            recordDto.setId(order.getId()); // 订单Id
            recordDto.setUserId(order.getUserId());
            recordDto.setDoctorId(order.getDoctorId());
            recordDto.setDoctorName(order.getDoctorName());
            recordDto.setAppointmentTime(order.getAppointmentTime());
            recordDto.setIsFinished(order.getIsFinished());

            User patient = userService.getById(order.getUserId());
            recordDto.setName(patient.getName());

            Doctor doctorInfo = doctorService.getById(order.getDoctorId());
            recordDto.setCombo(doctorInfo.getCombo());

            LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Department::getCombo,doctorInfo.getCombo());
            Department department = departmentService.getOne(wrapper);

            recordDto.setDeptName(department.getDeptName());
            recordDto.setHospitalName(department.getHospitalName());
            records.add(recordDto);
        }

        return R.success(records);
    }

    @PostMapping("/recordtoday")
    public R<List<RecordDto>> recordToday(@RequestBody User user) {
        log.info("记录今日");
        Long roleId = user.getRoleId();
        Long id = user.getId(); // 医生或者管理员

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);


        List<Orders> orders = null;
        if (roleId ==2 && id != null){
            // 管理员
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.between(Orders::getAppointmentTime, startOfDay, endOfDay);
            orders = orderService.list(wrapper);
        }else if (roleId ==1 && id != null){
            // 医生
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getDoctorId,id);
            wrapper.between(Orders::getAppointmentTime, startOfDay, endOfDay);
            orders = orderService.list(wrapper);
        }else {
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getUserId,id);
            wrapper.between(Orders::getAppointmentTime, startOfDay, endOfDay);
            orders = orderService.list(wrapper);
        }

        ArrayList<RecordDto> records = new ArrayList<>();
        for (Orders order : orders) {
            RecordDto recordDto = new RecordDto();

            recordDto.setId(order.getId()); // 订单Id
            recordDto.setUserId(order.getUserId());
            recordDto.setDoctorId(order.getDoctorId());
            recordDto.setDoctorName(order.getDoctorName());
            recordDto.setAppointmentTime(order.getAppointmentTime());
            recordDto.setIsFinished(order.getIsFinished());

            User patient = userService.getById(order.getUserId());
            recordDto.setName(patient.getName());

            Doctor doctorInfo = doctorService.getById(order.getDoctorId());
            recordDto.setCombo(doctorInfo.getCombo());

            LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Department::getCombo,doctorInfo.getCombo());
            Department department = departmentService.getOne(wrapper);

            recordDto.setDeptName(department.getDeptName());
            recordDto.setHospitalName(department.getHospitalName());
            records.add(recordDto);
        }

        return R.success(records);
    }
}

