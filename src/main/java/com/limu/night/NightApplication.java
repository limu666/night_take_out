package com.limu.night;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年09月30日 17:10
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableCaching  //开启spring Cache注解方式的缓存功能
public class NightApplication {
    public static void main(String[] args) {
        SpringApplication.run(NightApplication.class,args);
        log.info("项目启动成功！！");
    }
}
