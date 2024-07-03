package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.Orders;
import com.sunhy.mapper.OrdersMapper;
import com.sunhy.service.IOrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
