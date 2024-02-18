package com.limu.night.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.limu.night.dto.DishDto;
import com.limu.night.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

    //新增菜品，同时插入对应的口味数据，需要操作两张表：dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id来查询菜品信息以及所对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，提示更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);

    //通过ids删除指定菜品
    public void deleteByIds(List<Long> ids);

    ////修改菜品状态
    public void upStatus(List<Long> ids, int st);
}
