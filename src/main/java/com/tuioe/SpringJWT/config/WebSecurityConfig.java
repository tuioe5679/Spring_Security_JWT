package com.tuioe.SpringJWT.config;

import com.tuioe.SpringJWT.jwt.JwtAccessDeniedHandler;
import com.tuioe.SpringJWT.jwt.JwtAuthenticationEntryPoint;
import com.tuioe.SpringJWT.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 비밀번호 암호화 빈 생성
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // csrf: 정상적인 사용자가 의도치 않은 위조요청을 보냄
        // rest api의 서버라면 인증정보를 보관하지 않기 때문에 csrf를 설정할 필요가 없다
        http
                .csrf().disable()
                .httpBasic().disable()// http 사용 불가 -> https
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 세션 설정 STATELESS
                .and()
                    .exceptionHandling()// 예외 핸들링 객체 지정
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .authorizeRequests()
                    .antMatchers("/auth/**").permitAll()// 로그인 페이지 URL
                    .anyRequest().authenticated()// 그외 요청 URL은 인증된 사용자만 접근 가능
                .and()
                    .apply(new JwtSecurityConfig(tokenProvider));// JwtSecurityConfig에 tokenProvider 적용

        return http.build();
    }

}
