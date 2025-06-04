package com.example.newsfeed.config;

import com.example.newsfeed.filter.UserFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<Filter> UserFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // Filter 등록
        filterRegistrationBean.setFilter(new UserFilter());

        // Filter 등록 순서
        filterRegistrationBean.setOrder(1);

        // Filter 적용
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}