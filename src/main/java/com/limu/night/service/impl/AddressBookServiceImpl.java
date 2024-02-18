package com.limu.night.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limu.night.entity.AddressBook;
import com.limu.night.mapper.AddressBookMapper;
import com.limu.night.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
