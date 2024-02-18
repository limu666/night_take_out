package com.limu.night.config;

/**
 * TODO 类描述
 *
 * @author: LiMu
 * @createTime: 2022年09月30日 17:17
 *
 */

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.limu.night.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Slf4j
@Configuration
@EnableKnife4j
@EnableSwagger2
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /*
    * 静态资源映射
    *
    * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry night) {
        log.info("开始进行静态资源映射...");
        night.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        night.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        night.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        night.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /*
    * 扩展mvc框架的消息转换器
    * */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");

        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }

    @Bean
    public Docket createRestApi() {
        // 文档类型
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.limu.night.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("深夜食堂")
                .version("1.0")
                .contact(new Contact("limu","","2495853587@qq.com"))
                .description("深夜食堂外卖接口文档")
                .build();
    }
}
