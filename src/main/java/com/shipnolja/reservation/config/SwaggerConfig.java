package com.shipnolja.reservation.config;

import org.apache.coyote.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket SwaggerApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo()) //API Docu 및 작성자 정보 매
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shipnolja.reservation"))
                .paths(PathSelectors.any()) //controller package 전부
                //paths(PathSelectors.ant("/posts/**")).build() 추가도 가능
                .build();
    }
    private ApiInfo swaggerInfo(){
        return new ApiInfoBuilder().title("Rest Api Documentation")
                .description("shipnolja API 설명을 위한 문서입니다.")
                .build();
    }
}
