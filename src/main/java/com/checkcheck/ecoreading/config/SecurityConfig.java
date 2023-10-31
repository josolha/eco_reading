package com.checkcheck.ecoreading.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(
                "/h2-console/**",
                "/api-document/**",
                "/swagger-ui/**",
                "/static/**"
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청에 대해 접근을 허용
//                        .antMatchers("/login", "/signup", "/user").permitAll() // 로그인, 회원가입, 유저 페이지는 인증 없이 접근 허용
//                        .antMatchers("/h2-console/**").permitAll() // H2 콘솔 경로도 인증 없이 접근 허용
//                        .antMatchers("/test").hasAuthority("ROLE_USER") // '/test' 경로는 'ROLE_USER' 권한을 가진 사용자만 접근 가능
//                        .anyRequest().authenticated() // 나머지 요청은 모두 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/login") // 로그인 페이지 설정
                        .defaultSuccessUrl("/test") // 로그인 성공 시 이동할 기본 URL 설정
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동할 URL 설정
                        .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                )
                .csrf(csrf -> csrf.disable()) // H2 콘솔 사용 시 CSRF 비활성화 필요
                .headers(headers -> headers.frameOptions().disable()) // H2 콘솔은 iframe을 사용하기 때문에 이를 허용해야 함
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
