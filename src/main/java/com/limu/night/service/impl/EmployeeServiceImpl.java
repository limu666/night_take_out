package com.limu.night.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limu.night.entity.Employee;
import com.limu.night.mapper.EmployeeMapper;
import com.limu.night.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年09月30日 17:49
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
