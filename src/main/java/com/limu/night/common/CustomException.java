package com.limu.night.common;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月02日 22:15
 * 自定义业务异常类
 */


public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }

}
