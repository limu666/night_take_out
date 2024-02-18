package com.limu.night.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.limu.night.dto.OrdersDto;
import com.limu.night.entity.Orders;

public interface OrderService extends IService<Orders> {

    //用户下单
    public void submit(Orders orders);

    //查找detail
    public OrdersDto getByIdWithDetail(Long id);
}
