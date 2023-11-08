//package com.checkcheck.ecoreading.security.jwt;
//
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@RequiredArgsConstructor
//@Component
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final TokenProvider tokenProvider;
//    public static final String HEADER_AUTHORIZATION = "Authorization";
//    public static final String TOKEN_PREFIX = "Bearer ";
//    private final RedisTemplate redisTemplate;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 요청 헤더의 Authorization 키의 값 조회
//        String authorizationHeader =  request.getHeader(HEADER_AUTHORIZATION);
//        // 가져온 값에서 접두사 제거
//        String token = getAccessToken(authorizationHeader);
//        // 가져온 토큰이 유효한지 확인하고, 유효한 떄는 인증 정보 설정
//        if (tokenProvider.validToken(token)) {
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private String getAccessToken(String  authorizationHeader) {
//        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)){
//            return authorizationHeader.substring(TOKEN_PREFIX.length());
//        }
//        return null;
//    }
//}
//
