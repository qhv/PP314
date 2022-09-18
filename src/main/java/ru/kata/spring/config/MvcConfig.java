package ru.kata.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/styles/css/**")
                .addResourceLocations("classpath:/static/bootstrap/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/bootstrap/js/");
    }

    @Override public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin")
                .setViewName("admin/users");
        registry.addViewController("/user")
                .setViewName("user/user");
        registry.addViewController("/login")
                .setViewName("user/login");
        registry.addViewController("/")
                .setViewName("index");
        registry.addViewController("/index")
                .setViewName("index");
    }
}
