package com.limu.night.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limu.night.common.R;
import com.limu.night.dto.OrdersDto;
import com.limu.night.entity.OrderDetail;
import com.limu.night.entity.Orders;
import com.limu.night.entity.ShoppingCart;
import com.limu.night.service.OrderDetailService;
import com.limu.night.service.OrderService;
import com.limu.night.service.ShoppingCartService;
import com.limu.night.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月06日 22:29
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    //提交订单数据
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);

        orderService.submit(orders);

        return R.success("下单成功");

    }

    //前端页面进行分页查询
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        log.info("page = {},pageSize = {}", page, pageSize);

        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage,"records");

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto>  list = new ArrayList<>();

        //orderService.getByIdWithDetail(list);
        //查找detail
        for (Orders r : records) {
            OrdersDto detail = orderService.getByIdWithDetail(r.getId());
            list.add(detail);
        }

       /* List<OrdersDto>  list = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            return ordersDto;

        }).collect(Collectors.toList());*/


        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);

    }

    //进行分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String number,String beginTime,String endTime) {

        log.info("page = {},pageSize = {},number:{},beginTime:{},endTime:{}", page, pageSize,number,beginTime,endTime);

        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        //时间查询
        if(beginTime != null && endTime != null){
            queryWrapper.between(Orders::getOrderTime,beginTime,endTime);
      }

        //模糊查询
        queryWrapper.like(number != null,Orders::getId,number);

        //根据时间排序
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage,"records");

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto>  list = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            return ordersDto;

        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }


    //修改订单信息状态
    @PutMapping
    public R<String> putOrder(@RequestBody Orders orders){
        log.info("信息为：{}",orders.getId());
        if(orders != null){
            Orders order = orderService.getById(orders.getId());
            order.setStatus(order.getStatus()+1);
            orderService.updateById(order);
            return R.success("修改成功");
        }

        return R.error("信息错误");
    }

    //再来一单
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        log.info("数据：{}",orders);
        OrdersDto ordersDto = orderService.getByIdWithDetail(orders.getId());
        List<ShoppingCart> cart = new ArrayList<>();
        for(OrderDetail c : ordersDto.getOrderDetails()){
            ShoppingCart cart1 = new ShoppingCart();
            cart1.setName(c.getName());
            cart1.setImage(c.getImage());
            cart1.setUserId(ordersDto.getUserId());
            cart1.setDishId(c.getDishId());
            cart1.setSetmealId(c.getSetmealId());
            cart1.setDishFlavor(c.getDishFlavor());
            cart1.setAmount(c.getAmount());
            shoppingCartService.save(cart1);
        }

        return R.success("成功");
    }

    }
