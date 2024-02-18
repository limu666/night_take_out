package com.limu.night.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limu.night.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.Base64;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月06日 10:23
 */

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
