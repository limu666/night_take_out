package com.limu.night.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.limu.night.common.BaseContext;
import com.limu.night.common.R;
import com.limu.night.entity.ShoppingCart;
import com.limu.night.mapper.ShoppingCartMapper;
import com.limu.night.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月06日 10:26
 */

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据：{}",shoppingCart);

        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if(dishId != null){
            //添加购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if(cartServiceOne != null){
            //如果已经存在，就在原来的数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    //查看购物车
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        System.out.println(BaseContext.getCurrentId());

        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    //清空购物车
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }

    //减少菜品或者套餐
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("菜品id为：{},套餐id为：{}",shoppingCart.getDishId(),shoppingCart.getSetmealId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //通过菜品id查找菜品购物车指定商品信息
        queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        ShoppingCart cart = new ShoppingCart();

        //获取商品信息
        ShoppingCart one = this.shoppingCartService.getOne(queryWrapper);
        if(one != null){
            if(one.getNumber() == 1){
                shoppingCartService.remove(queryWrapper);
            }else{
                one.setNumber(one.getNumber()-1);
                shoppingCartService.updateById(one);
                cart = shoppingCartService.getById(one);
            }
        }else{
            LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

            ShoppingCart two = this.shoppingCartService.getOne(lambdaQueryWrapper);
            if(two.getNumber() == 1){
                shoppingCartService.remove(lambdaQueryWrapper);
            }else{
                two.setNumber(two.getNumber()-1);
                shoppingCartService.updateById(two);
                cart = shoppingCartService.getById(two);
            }
        }
        return R.success(cart);
    }

}
