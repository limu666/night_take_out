package com.limu.night.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limu.night.entity.User;
import com.limu.night.mapper.UserMapper;
import com.limu.night.service.UserService;
import org.springframework.stereotype.Service;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月05日 15:08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
