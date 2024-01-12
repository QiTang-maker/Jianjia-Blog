package com.dyj.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dyj.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("蒹葭博客后台", "http://www.dyj.com", "jianjia@dyj.com");
        return new ApiInfoBuilder()
                .title("蒹葭博客")
                .description("蒹葭博客后台说明文档")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
