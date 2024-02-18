package com.limu.night.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.limu.night.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
