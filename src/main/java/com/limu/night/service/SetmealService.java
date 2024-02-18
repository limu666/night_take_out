package com.limu.night.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.limu.night.dto.SetmealDto;
import com.limu.night.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //将套餐基本信息和关联的菜品信息一块保存
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐操作,同时需要删除套餐和菜品的关联数据
    public void removeWithDish(List<Long> ids);

    //停售--启售修改套餐状态
    public void upStatus(List<Long> ids, int st);
    //通过id获取套餐信息及对应的菜品信息
    public SetmealDto getByIdWithDish(Long id);

    //修改套餐信息及菜品
    public void updateWithDish(SetmealDto setmealDto);
}
