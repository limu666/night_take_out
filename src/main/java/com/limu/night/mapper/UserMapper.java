package com.limu.night.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limu.night.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月05日 15:06
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
