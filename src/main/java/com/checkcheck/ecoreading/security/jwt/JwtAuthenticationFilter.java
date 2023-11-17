package com.checkcheck.ecoreading.security.jwt;


import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
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
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String accessToken = resolveToken(httpRequest, "accessToken");
        String refreshToken = resolveToken(httpRequest, "refreshToken");

        if (isTokenValid(accessToken)) {
            processValidAccessToken(accessToken, httpRequest);
        } else if (StringUtils.hasText(refreshToken) && isRefreshTokenValid(refreshToken, httpRequest)) {
            refreshTokenAndProcess(httpResponse, refreshToken, httpRequest);
        }
        chain.doFilter(request, response);
    }

    private boolean isTokenValid(String token) {
        return StringUtils.hasText(token) && jwtTokenProvider.validateToken(token);
    }

    private boolean isRefreshTokenValid(String refreshToken, HttpServletRequest httpRequest) {
        String userEmail = jwtTokenProvider.getSubject(refreshToken);
        String storedRefreshToken = redisTemplate.opsForValue().get("RefreshToken:" + userEmail);
        return refreshToken.equals(storedRefreshToken) && jwtTokenProvider.validateToken(refreshToken);
    }

    private void processValidAccessToken(String accessToken, HttpServletRequest httpRequest) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userEmail = jwtTokenProvider.getSubject(accessToken);
        Optional<Users> user = userRepository.findByEmail(userEmail);
        user.ifPresent(u -> httpRequest.setAttribute("userId", u.getUsersId()));
    }

    private void refreshTokenAndProcess(HttpServletResponse httpResponse, String refreshToken, HttpServletRequest httpRequest) {
        String userEmail = jwtTokenProvider.getSubject(refreshToken);
        Optional<Users> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            Long userId = user.get().getUsersId(); // userId 추출
            Collection<GrantedAuthority> authorities = jwtTokenProvider.getRoles(refreshToken);
            String newAccessToken = jwtTokenProvider.createAccessToken(userEmail, authorities, userId);
            addTokenToCookie(httpResponse, "accessToken", newAccessToken);
            processValidAccessToken(newAccessToken, httpRequest);
        }
    }

    private void addTokenToCookie(HttpServletResponse response, String name, String token) {
        Cookie cookie = new Cookie(name, token);
        // 쿠키 설정
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge(10);
        // 필요한 경우 쿠키의 보안 설정을 여기에 추가
        response.addCookie(cookie);
    }

    private String resolveToken(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
