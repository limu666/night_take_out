package com.limu.test;

import org.junit.jupiter.api.Test;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年10月03日 18:59
 */

public class UploadFileTest {

    @Test
    public void test1(){
        String fileName = "hello.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }

}
