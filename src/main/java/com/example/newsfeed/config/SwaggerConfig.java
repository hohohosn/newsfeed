package com.example.newsfeed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("뉴스피드 API")
                        .description("뉴스피트 프로젝트 API 문서입니다.")
                        .version("1.0.0"));
    }
}
