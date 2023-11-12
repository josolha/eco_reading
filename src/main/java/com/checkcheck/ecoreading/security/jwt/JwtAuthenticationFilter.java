package com.checkcheck.ecoreading.security.jwt;


import java.io.IOException;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {


    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("httpRequest"+httpRequest);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        log.info("httpResponse"+httpResponse);
        // 로그인, 회원가입, 로그아웃, 유저 페이지 등 인증이 필요없는 경로 무시
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/user/login") || requestURI.startsWith("/user/signup")
                || requestURI.startsWith("/user/logout")) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = resolveToken(httpRequest);
        log.info("accessToken============" +accessToken);
        if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            // Access 토큰이 유효한 경우, 인증 처리
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            // 액세스 토큰이 존재하지 않거나 유효하지 않은 경우
            processInvalidAccessToken(httpRequest, httpResponse, chain);
        }
    }

    private void processInvalidAccessToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain)
            throws IOException, ServletException {
        String refreshToken = findRefreshToken(httpRequest);
        log.info("refreshToken============" +refreshToken);

        if (StringUtils.hasText(refreshToken)) {
            String userEmail = jwtTokenProvider.getSubject(refreshToken);
            String storedRefreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:" + userEmail);

            if (refreshToken.equals(storedRefreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
                Collection<GrantedAuthority> authorities = jwtTokenProvider.getRoles(refreshToken);
                String newAccessToken = jwtTokenProvider.createAccessToken(userEmail, authorities);
                log.info("newAccessToken===="+newAccessToken);

                Cookie newCookie = new Cookie("accessToken", newAccessToken);
                newCookie.setHttpOnly(false);
                newCookie.setPath("/");
                newCookie.setSecure(false);
                newCookie.setMaxAge(10); // 쿠키 수명을 10초로 설정
                httpResponse.addCookie(newCookie);

                Authentication newAuthentication = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
                chain.doFilter(httpRequest, httpResponse);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid Session");
            }
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid Session");
        }
    }
    // 1.저장된 쿠키에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("===cookie==="+ cookie);
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    private String findRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}