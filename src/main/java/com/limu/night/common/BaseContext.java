package com.limu.night.common;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月02日 15:18
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //设置id值
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    //获取id值
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
