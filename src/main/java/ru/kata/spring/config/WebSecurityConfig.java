package ru.kata.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                .disable()
                .authorizeHttpRequests(
                        urlConfig -> urlConfig
                                .antMatchers("/js/**", "/styles/css/**")
                                .permitAll()
                                .antMatchers("/", "/index", "/login")
                                .permitAll()
                                .antMatchers("/user/**", "/api/v1/principal")
                                .hasAnyRole("USER", "ADMIN")
                                .antMatchers("/admin/**", "/api/v1/admin/**")
                                .hasRole("ADMIN")
                                .anyRequest()
                                .denyAll()
                )
                .formLogin(
                        login -> login
                                .loginPage("/login")
                                .successHandler(successUserHandler)
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}