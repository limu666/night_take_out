package com.limu.night.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limu.night.entity.Orders;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
