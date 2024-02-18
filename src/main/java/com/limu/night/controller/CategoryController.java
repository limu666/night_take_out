package com.limu.night.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limu.night.common.R;
import com.limu.night.entity.Category;
import com.limu.night.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月02日 16:02
 */

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    //新增分类
    @PostMapping
    public R<Object> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功...");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    //根据id删除分类
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);

        //categoryService.removeById(id);
        categoryService.remove(id);

        return R.success("分类信息删除成功...");
    }

    //根据id修改分类信息
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    //根据条件查询分类数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        //根据添加条件动态查询分类数据
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }

}
