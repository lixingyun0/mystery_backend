package com.xingyun.mysteryapi.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("mystery").description("接口说明")
                .termsOfServiceUrl("http://www.baidu.com")
                // .contact(contact)
                .version("1.0.0").build();
    }

    @Bean
    public Docket app() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .apiInfo(apiInfo())
                .groupName("mystery")
                .forCodeGeneration(true)
                //.extensions(openApiExtensionResolver.buildExtensions("WoWarriors"))
                ;
    }
}