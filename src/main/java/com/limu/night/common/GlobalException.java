package com.limu.night.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月01日 10:40
 * 全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
@Slf4j
public class GlobalException {

    /*
    * 异常处理方法
    * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split((" "));
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.info(ex.getMessage());

        return R.error(ex.getMessage());
    }

    //未成功抓获
    @ExceptionHandler(FileNotFoundException.class)
    public R<String> exceptionHandler(FileNotFoundException ex){
        log.info("图片丢失：{}",ex.getMessage());
        return R.error("图片丢失,问题不大...");
    }

}
