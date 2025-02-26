package com.jixiata.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@WebAppConfiguration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = "com.jixiata.controller")
public class SwaggerConfig {

        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("家校沟通平台接口文档")
                    .description("Home And School Communication Platform")
                    .version("1.0.0")
                    .termsOfServiceUrl("")
                    .license("")
                    .licenseUrl("")
                    .build();
        }

}
