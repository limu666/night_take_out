package com.limu.night.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limu.night.common.R;
import com.limu.night.dto.DishDto;
import com.limu.night.entity.Category;
import com.limu.night.entity.Dish;
import com.limu.night.entity.DishFlavor;
import com.limu.night.service.CategoryService;
import com.limu.night.service.DishFlavorService;
import com.limu.night.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月03日 19:36
 */

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){  //此处因为提交的是Jason数据所以需要加注解
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        //清理某个分类下面的菜品缓存数据
        String key = "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        return R.success("新增菜品成功...");
    }

    //菜品信息的分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        //分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //添加构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        //stream流
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();

                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    //根据id查询菜品信息和对应的口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){  //此处因为提交的是Jason数据所以需要加注解
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        //清理所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        //清理某个分类下面的菜品缓存数据
        /*String key = "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);*/

        return R.success("修改菜品成功...");
    }

    //修改菜品状态
    @PostMapping("/status")
    public R<String> upStatus(@RequestParam  List<Long> ids,int st){
        log.info("菜品id为：{}，菜品状态改为：{}",ids,st);
        /*if(ids.length != 0 && !"".equals(st) ){ //判断是否为null，为null则error
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
            return R.success("success");
        }*/
        dishService.upStatus(ids, st);
        //清理所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("success");
    }

    //通过ids删除指定菜品
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam List<Long> ids){
        /*if(ids.length != 0) { //判断是否为null，为null则error
            List<Dish> dishs = new ArrayList<>(); //创建list集合，准备将菜品信息放进去
            for (long c : ids) {
                dishs.add(dishService.getById(c));
            }
            for (Dish d: dishs) {
                dishService.removeById(d);
            }
            return R.success("success");
        }*/
        dishService.deleteByIds(ids);
        //清理所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("success");
    }

    /*//根据条件查询对应的菜品数据
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（售卖）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }*/
    //根据条件查询对应的菜品数据
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList = null;

        //动态构造key
        String key = "dish_" + dish.getCategoryId()+"_" +dish.getStatus();
        //先从redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if (dishDtoList != null){
            //如果存在，直接返回，无需查询数据库
            return R.success(dishDtoList);
        }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（售卖）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        //stream流
        dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到redis
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

}
