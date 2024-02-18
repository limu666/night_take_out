package com.limu.night.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limu.night.common.CustomException;
import com.limu.night.dto.DishDto;
import com.limu.night.entity.Dish;
import com.limu.night.entity.DishFlavor;
import com.limu.night.entity.Setmeal;
import com.limu.night.mapper.DishMapper;
import com.limu.night.service.DishFlavorService;
import com.limu.night.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月02日 21:34
 */

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    //新增菜品，同时插入对应的口味数据，需要操作两张表：dish dish_flavor
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    //根据id来查询菜品信息以及所对应的口味信息
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto =new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    //更新菜品信息，提示更新对应的口味信
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish基本信息
        this.updateById(dishDto);
        //先清理当前菜品对应的口味数据----dish——flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据--dish——flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    //通过ids删除指定菜品
    @Transactional
    public void deleteByIds(List<Long> ids) {
        //查询菜品状态，确定是否可以删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个异常
            throw new CustomException("菜品正在售卖中，不能删除");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        //删除关系表中的数据
        dishFlavorService.remove(lambdaQueryWrapper);

    }

    //修改菜品状态
    @Transactional
    public void upStatus(List<Long> ids, int st) {
       List<Dish> dishs = new ArrayList<>(); //创建list集合，准备将菜品信息放进去
            //查询所有菜品信息
            for (long c : ids) {
                dishs.add(dishService.getById(c));
            }
            //log.info("基本信息为：{}",id);
            for (Dish d: dishs) {
                //将指定菜品id修改状态
                d.setStatus(st);
                dishService.updateById(d);
            }
    }
}
