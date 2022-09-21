package com.tuioe.SpringJWT.config;

import com.tuioe.SpringJWT.jwt.JwtFliter;
import com.tuioe.SpringJWT.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    // SecurityConfig에 필터를 등록하여 스프링 시큐리티 필터에 적용
    @Override
    public void configure(HttpSecurity http){
        JwtFliter customFilter = new JwtFliter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
